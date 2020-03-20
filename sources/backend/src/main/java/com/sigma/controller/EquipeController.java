package com.sigma.controller;

import java.util.ArrayList;
import java.util.List;

import com.sigma.model.*;
import com.sigma.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sigma.dto.EquipeDto;
import com.sigma.util.IterableToList;



@Controller
@RequestMapping("/api/team")
public class EquipeController {

	@GetMapping
	@ResponseBody
	public String list() throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			List<Equipe> teams = IterableToList.toList(equipeRepository.findAll());
			return objectMapper.writeValueAsString(teams);
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find teams",
							ex)
			);
		}
	}

	/**
	 * POST /create  --> Create a new team and save it in the database.
	 */
	@PostMapping
	@ResponseBody
	public String create(@RequestBody EquipeDto equipe) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			Acheteur ach = acheteurRepository.findOne(equipe.getResponsable());
			Entite en = entiteRepository.findById(equipe.getEntity());
			List<Acheteur> liste = new ArrayList<Acheteur>();
			for (Long ache: equipe.getMembres()) {
				Acheteur a = acheteurRepository.findOne(ache);
				a.setEntite(en);
				liste.add(a);
			}

			Equipe eq = new Equipe(equipe.getLibelle(), ach, en, liste);
			//chooseResponsable(eq.getId(), ach.getId());

			eq.getResponsable().setRole(roleRepository.findByName(RoleType.ROLE_RESPONSABLE_ACHAT.toString()));
			eq.getResponsable().setEntite(en);

			Long id = equipeRepository.save(eq).getId();
			addMember(id, eq.getResponsable().getId());
			for (Long acheteur: equipe.getMembres()) {
				addMember(id, acheteur);
			}

		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to create team",
							ex)
			);
		}

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						"Team successfully created\nReceived input:\n" + objectMapper.writeValueAsString(equipe))
		);
	}

	/**
	 * GET /delete  --> Delete the user having the passed id.
	 */
	@RequestMapping("/delete/{id}")
	@ResponseBody
	public String delete(@PathVariable Long id) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			Equipe eq = equipeRepository.findOne(id);

			eq.getResponsable().setRole(roleRepository.findByName(RoleType.ROLE_ACHETEUR.toString()));

			for (int k = 0; k < eq.getMembres().size(); ++k) {
				removeMember(id, eq.getMembres().get(k).getId());
			}

			equipeRepository.delete(eq);

		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Error when deleting the team",
							ex)
			);
		}

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						"Team successfully deleted")
		);
	}


	/**
	 * GET /update  --> Update the email and the name for the user in the
	 * database having the passed id.
	 */
	@PostMapping("/update/{id}")
	@ResponseBody
	public String update(@PathVariable Long id, @RequestBody EquipeDto equipe) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			Acheteur ra = acheteurRepository.findOne(equipe.getResponsable());
			Entite en = entiteRepository.findById(equipe.getEntity());
			List<Acheteur> liste = new ArrayList<Acheteur>();
			for (Long ach: equipe.getMembres()) {
				Acheteur a = acheteurRepository.findOne(ach);
				a.setEntite(en);
				liste.add(a);
			}

			Equipe eq = new Equipe(equipe.getLibelle(), ra, en, liste);

			/*Equipe old = equipeRepository.findOne(id);
			old.getResponsable().setRole(roleRepository.findByName(RoleType.ROLE_ACHETEUR.toString()));
			equipeRepository.delete(old);*/
			delete(id);
			eq.getResponsable().setRole(roleRepository.findByName(RoleType.ROLE_RESPONSABLE_ACHAT.toString()));
			eq.getResponsable().setEntite(en);
			Long idEq = equipeRepository.save(eq).getId();
			addMember(idEq, eq.getResponsable().getId());
			for (Long acheteur: equipe.getMembres()) {
				addMember(idEq, acheteur);
			}
		}
		catch (Exception ex) {
			return "Error updating the team: " + ex.toString();
		}
		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						"Team successfully updated")
		);
	}

	@PutMapping("/members")
	@ResponseBody
	public String addMember(@RequestParam Long id, @RequestParam long acheteur_id) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			Equipe eq = equipeRepository.findById(id);
			Acheteur a = acheteurRepository.findOne(acheteur_id);
			if (a.getEquipe() == eq) {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.BAD_REQUEST,
								"Le membre fait déjà partie de cette équipe")
				);
			} else {
				eq.addMembre(a);
				a.setEquipe(eq);
				acheteurRepository.save(a);
				equipeRepository.save(eq);
			}
		} catch (Exception e) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Error",
							e)
			);
		}

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						"Membre ajouté avec succès")
		);
	}

	@GetMapping("/members")
	@ResponseBody
	public String listMembers(@RequestParam Long id) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			List<Acheteur> acheteurs = acheteurRepository.findByEquipeId(id);
			return objectMapper.writeValueAsString(acheteurs);
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find members",
							ex)
			);
		}
	}

	@DeleteMapping("/members")
	@ResponseBody
	public String removeMember(@RequestParam Long id, @RequestParam long acheteur_id) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			Equipe eq = equipeRepository.findById(id);
			Acheteur a = acheteurRepository.findOne(acheteur_id);
			a.setEquipe(null);
			a.setEntite(null);
			acheteurRepository.save(a);
			equipeRepository.save(eq);

		} catch (Exception e) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Error",
							e)
			);
		}
		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						"Membre retiré avec succès")
		);
	}

	@RequestMapping("/responsable")
	@ResponseBody
	public String chooseResponsable(@RequestParam Long id, @RequestParam long acheteur_id) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			Equipe eq = equipeRepository.findById(id);
			Acheteur oldResp = eq.getResponsable();
			if(oldResp != null) {
				Acheteur usr = new Acheteur(oldResp.getNom(), oldResp.getPrenom(),
						oldResp.getMail(), oldResp.getAdresse(), oldResp.getTelephone(), oldResp.getMobile(),
						oldResp.getDateEnregistrement(), oldResp.getEquipe(), oldResp.getEntite(),oldResp.getSegments());
				usr.setPassword(oldResp.getPassword());
				acheteurRepository.delete(oldResp);
				usr.setRole(roleRepository.findByName(RoleType.ROLE_ACHETEUR.toString()));
				acheteurRepository.save(usr);
			}
			Acheteur responsableAchat = acheteurRepository.findOne(acheteur_id);
			responsableAchat.setRole(roleRepository.findByName(RoleType.ROLE_RESPONSABLE_ACHAT.toString()));
			eq.setResponsable(responsableAchat);

			acheteurRepository.save(responsableAchat);
			equipeRepository.save(eq);


		} catch (Exception e) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Error",
							e)
			);
		}

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						"Responsable d'achat ajouté à l'equipe avec succès")
		);
	}

	@GetMapping("/searchByName")
	@ResponseBody
	public String searchTeamByName(@RequestParam String name) throws com.fasterxml.jackson.core.JsonProcessingException {
		if (name == null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"Le paramètre 'name' n'est pas fourni")
			);
		}

		List<Equipe> teams = equipeRepository.findByLibelle(name);

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						objectMapper.writeValueAsString(teams))
		);
	}

	@GetMapping("/searchById")
	@ResponseBody
	public String searchTeamByID(@RequestParam Long id) throws com.fasterxml.jackson.core.JsonProcessingException {
		if (id == null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"Le paramètre 'id' n'est pas fourni")
			);
		}

		Equipe team = equipeRepository.findOne(id);

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						objectMapper.writeValueAsString(team))
		);
	}

	// Private fields

	@Autowired
	private EquipeRepository equipeRepository;

	@Autowired
	private ResponsableAchatRepository responsableRepository;

	@Autowired
	private EntiteRepository entiteRepository;

	@Autowired
	private AcheteurRepository acheteurRepository;

	@Autowired
	private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

	@Autowired
	RoleRepository roleRepository;
}