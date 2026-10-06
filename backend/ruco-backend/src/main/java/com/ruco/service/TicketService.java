package com.ruco.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruco.config.CustomUserDetails;
import com.ruco.model.ChatMessage;
import com.ruco.model.Ticket;
import com.ruco.repo.ChatMessageRepository;
import com.ruco.repo.TicketRepository;
import com.ruco.request.ChatRequest;
import com.ruco.request.SearchRequest;
import com.ruco.request.TicketRequest;
import com.ruco.response.ChatResponse;
import com.ruco.response.TicketResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {

	private final TicketRepository ticketRepository;
	private final UserService userService;
	private final ChatMessageRepository chatMessageRepository;
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	public void create(TicketRequest request) {
		String subject = request.getSubject();
		String description = request.getDescription();
		CustomUserDetails user = userService.getUser();
		Long id = user.getId();
		Ticket ticket = new Ticket(df.format(LocalDateTime.now()), subject, description, id);
		ticketRepository.save(ticket);
	}

	@Transactional(readOnly = true)
	public Page<?> get(SearchRequest request) {
		Integer limit = request.getLimit();
		Integer offset = request.getOffset();
		Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "saveDate"));
		Page<Ticket> pages = ticketRepository.findAllByIsDeleteFalse(pageable);
		return pages.map(this::mapData);
	}

	private TicketResponse mapData(Ticket ticket) {
		TicketResponse r = new TicketResponse();
		r.setId(ticket.getId());
		r.setTicketId("T" + ticket.getTicketId());
		r.setSubject(ticket.getSubject());
		r.setDescription(ticket.getDescription());
		r.setDate(ticket.getSaveDate());
		r.setStatus(ticket.getStatus());
		return r;
	}

	public TicketResponse get(Long id) {
		Optional<Ticket> ticketOptional = ticketRepository.findById(id);
		if (ticketOptional.isPresent()) {
			Ticket ticket = ticketOptional.get();
			TicketResponse response = mapData(ticket);
			List<ChatMessage> chats = ticket.getChats();
			List<ChatResponse> chatResponse = chats.stream().map(chat -> {
				String username = chat.getUsername();
				String message = chat.getMessage();
				LocalDateTime saveDate = chat.getSaveDate();
				ChatResponse r = new ChatResponse();
				r.setUsername(username);
				r.setDate(saveDate);
				r.setMessage(message);
				return r;
			}).collect(Collectors.toList());
			response.setChats(chatResponse);
			return response;
		}
		return null;
	}

	public void delete(Long id) {
		Optional<Ticket> ticketOptional = ticketRepository.findById(id);
		if (ticketOptional.isPresent()) {
			Ticket ticket = ticketOptional.get();
			ticket.setIsDelete(true);
			ticketRepository.save(ticket);
		}
	}

	public ChatResponse sendMsg(ChatRequest request) {
		CustomUserDetails user = userService.getUser();
		Long userId = user.getId();
		String username = user.getUsername();
		Long id = request.getId();
		Optional<Ticket> ticketOptional = ticketRepository.findById(id);
		if (ticketOptional.isPresent()) {
			Ticket ticket = ticketOptional.get();
			ChatMessage chat = new ChatMessage();
			chat.setMessage(request.getText());
			chat.setUserId(userId);
			chat.setUsername(username);
			chat.setTicket(ticket);
			chat = chatMessageRepository.save(chat);
			return map(chat);
		}
		return null;
	}

	private ChatResponse map(ChatMessage chat) {
		String username = chat.getUsername();
		String message = chat.getMessage();
		LocalDateTime saveDate = chat.getSaveDate();
		ChatResponse r = new ChatResponse();
		r.setUsername(username);
		r.setDate(saveDate);
		r.setMessage(message);
		return r;
	}

	public void closeTicket(Long id) {
		Optional<Ticket> ticketOptional = ticketRepository.findById(id);
		if (ticketOptional.isPresent()) {
			Ticket ticket = ticketOptional.get();
			ticket.setStatus("CLOSE");
			ticketRepository.save(ticket);
		}
	}

}
