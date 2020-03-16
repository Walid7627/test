package com.sigma.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sigma.dto.EquipeDto;
import com.sigma.model.Acheteur;
import com.sigma.model.ApiResponse;
import com.sigma.model.Equipe;
import com.sigma.model.ResponsableAchat;
import com.sigma.model.RoleType;
import com.sigma.repository.AcheteurRepository;
import com.sigma.repository.EquipeRepository;
import com.sigma.repository.ResponsableAchatRepository;
import com.sigma.repository.RoleRepository;
import com.sigma.util.IterableToList;



@Controller
@RequestMapping("/api/teams")
public class EquipeController {

	@GetMapping
	@ResponseBody
	public String list() throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			List<Equipe> users = IterableToList.toList(equipeRepository.findAll());
			return objectMapper.writeValueAsString(users);
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find teams",
							ex)
					);
		}
	}

	/**
	 * GET /create  --> Create a new user and save it in the database.
	 */
	@PostMapping
	@ResponseBody
	public String create(@RequestBody EquipeDto equipe) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			Equipe eq = new Equipe(equipe.getLibelle(), equipe.getResponsable(), equipe.getMembres());

			equipeRepository.save(eq);
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
	@DeleteMapping
	@ResponseBody
	public String delete(@RequestParam Long id) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			Equipe eq = equipeRepository.findOne(id);
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
	@PutMapping
	@ResponseBody
	public String update(@RequestParam Long id, @RequestBody EquipeDto equipe) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			Equipe eq = new Equipe(equipe.getLibelle(), equipe.getResponsable(), equipe.getMembres());

			Equipe old = equipeRepository.findOne(id);
			equipeRepository.delete(old);
			equipeRepository.save(eq);
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

			if (eq.getMembres().contains(a)) {
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

			if (!eq.getMembres().contains(a)) {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.BAD_REQUEST,
								"Le membre ne fait pas partie de cette équipe")
						);
			} else {
				eq.removeMembre(a);
				a.setEquipe(null);
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
						"Membre retiré avec succès")
				);
	}

	@RequestMapping("/responsable")
	@ResponseBody
	public String chooseResponsable(@RequestParam Long id, @RequestParam long acheteur_id) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			Equipe eq = equipeRepository.findById(id);
			ResponsableAchat oldResp = eq.getResponsable();
			if(oldResp != null) {
				Acheteur usr = new Acheteur(oldResp.getNom(), oldResp.getPrenom(),
						oldResp.getMail(), oldResp.getAdresse(), oldResp.getTelephone(), oldResp.getMobile(),
						oldResp.getDateEnregistrement(), oldResp.getEquipe(), oldResp.getEntite(),oldResp.getSegments());
				usr.setPassword(oldResp.getPassword());
				responsableRepository.delete(oldResp);
				usr.setRole(roleRepository.findByName(RoleType.ROLE_ACHETEUR.toString()));
				acheteurRepository.save(usr);
			}
			Acheteur responsableAchat = acheteurRepository.findOne(acheteur_id);

			ResponsableAchat usr = new ResponsableAchat(responsableAchat.getNom(), responsableAchat.getPrenom(),
					responsableAchat.getMail(), responsableAchat.getAdresse(), responsableAchat.getTelephone(),
					responsableAchat.getMobile(),
					responsableAchat.getDateEnregistrement(), responsableAchat.getEquipe(), responsableAchat.getEntite(), responsableAchat.getSegments());
			usr.setPassword(responsableAchat.getPassword());
			acheteurRepository.delete(responsableAchat);
			usr.setRole(roleRepository.findByName(RoleType.ROLE_RESPONSABLE_ACHAT.toString()));
			eq.setResponsable(usr);

			responsableRepository.save(usr);
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
	private AcheteurRepository acheteurRepository;

	@Autowired
	private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

	@Autowired
	RoleRepository roleRepository;
}
