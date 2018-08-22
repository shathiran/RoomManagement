package com.sharan.room.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sharan.room.enumeration.Status;
import com.sharan.room.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	public List<Member> findAll();
	
	public Member findByEmailAndPassword(String email, String password);

	public Member findById(Long id);
	
	public List<Member> findByStatus(Status status);

	@Query("select m.name from Member m where m.userType='MEMBER' order by m.name asc")
	public List<String> findAllNameAsc();
	
}