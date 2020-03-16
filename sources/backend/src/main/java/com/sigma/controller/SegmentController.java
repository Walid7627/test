package com.sigma.controller;

import com.sigma.dto.SegmentDto;
import com.sigma.model.ApiResponse;
import com.sigma.model.Segment;
import com.sigma.repository.SegmentRepository;
import com.sigma.util.IterableToList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;



@Controller
@RequestMapping("/segment")
public class SegmentController {

    @RequestMapping("/list")
    @ResponseBody
    public String list() throws com.fasterxml.jackson.core.JsonProcessingException {
        try {
            List<Segment> users = IterableToList.toList(segmentRepository.findAll());
            return objectMapper.writeValueAsString(users);
        } catch (Exception ex) {
          return objectMapper.writeValueAsString(
            new ApiResponse(HttpStatus.BAD_REQUEST,
                            "Unable to find users",
                            ex)
          );
        }
    }

    /**
     * GET /create  --> Create a new user and save it in the database.
     */
    @RequestMapping("/create")
    @ResponseBody
    public String create(@RequestBody SegmentDto segment) throws com.fasterxml.jackson.core.JsonProcessingException {
      try {
          Segment s = new Segment(segment.getLibelle(), segment.getCodeCPV(),
                                  segment.getCodeAPE(), segment.getMetriques());

          segmentRepository.save(s);
      } catch (Exception ex) {
        return objectMapper.writeValueAsString(
          new ApiResponse(HttpStatus.BAD_REQUEST,
                          "Unable to create user",
                          ex)
        );
      }

      return objectMapper.writeValueAsString(
        new ApiResponse(HttpStatus.OK,
                        "Team successfully created\nReceived input:\n" + objectMapper.writeValueAsString(segment))
      );
    }

    /**
     * GET /delete  --> Delete the user having the passed id.
     */
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam Long id) throws com.fasterxml.jackson.core.JsonProcessingException {
        try {
            Segment s = segmentRepository.findOne(id);
            segmentRepository.delete(s);
        } catch (Exception ex) {
          return objectMapper.writeValueAsString(
            new ApiResponse(HttpStatus.BAD_REQUEST,
                          "Error when deleting the user",
                          ex)
          );
        }

        return objectMapper.writeValueAsString(
          new ApiResponse(HttpStatus.OK,
                          "User successfully deleted")
        );
    }


    /**
     * GET /update  --> Update the email and the name for the user in the
     * database having the passed id.
     */
    @RequestMapping("/update")
    @ResponseBody
    public String updateUser(@RequestParam Long id, @RequestBody SegmentDto segment) throws com.fasterxml.jackson.core.JsonProcessingException {
        return objectMapper.writeValueAsString(
          new ApiResponse(HttpStatus.BAD_REQUEST,
                          "Not implemented")
        );
    }

    // Private fields

    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
}
