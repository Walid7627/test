package com.sigma.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import com.sigma.model.Segment;


@Service

public interface SegmentRepository extends PagingAndSortingRepository<Segment, Long> {
	List<Segment> findByLibelle(String libelle);
	
	List<Segment> findByCpv(String cpv);
	
	List<Segment> findByApe(String ape);
	
	
}
