package com.ruco.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ruco.model.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

}
