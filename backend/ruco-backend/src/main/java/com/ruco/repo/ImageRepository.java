package com.ruco.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ruco.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long>{

}
