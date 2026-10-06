import { CommonModule } from '@angular/common';
import { Component, effect, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { A11yModule } from '@angular/cdk/a11y';
import { TicketService } from '../../../services/ticket.service';
import { ActivatedRoute } from '@angular/router';
import { MatRipple } from '@angular/material/core';

@Component({
  selector: 'app-ticket-edit',
  templateUrl: './ticket-edit.component.html',
  styleUrl: './ticket-edit.component.scss',
  imports: [CommonModule, FormsModule, A11yModule, MatRipple],
})
export class TicketEditComponent implements OnInit {
  messages: any[] = [];
  username = signal('You');
  private ticketService = inject(TicketService);
  private route = inject(ActivatedRoute);
  id!: number;
  newMessage = '';
  ticket: any = {};

  constructor() {
    this.id = +this.route.snapshot.paramMap.get('id')!;
    effect(() => {
      const chatBox = document.querySelector('.chat-box');
      if (chatBox) chatBox.scrollTop = chatBox.scrollHeight;
    });
    this.username.set(JSON.parse(localStorage.getItem('user')!).username);
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    this.ticketService.getById(this.id).subscribe(
      (res) => {
        this.ticket = res;
        this.messages = res.chats;
      },
      (err) => {
        console.log(err);
      }
    );
  }

  sendMessage() {
    const text = this.newMessage.trim();
    if (!text) return;
    this.ticketService.sendMsg({ id: this.id, text }).subscribe(
      (res) => {
        console.log(res);
        this.messages.push(res);
        this.newMessage = '';
      },
      (err) => {
        console.log(err);
      }
    );

    // setTimeout(() => {
    //   this.messages.update((msgs) => [
    //     ...msgs,
    //     // {
    //     //   sender: 'support',
    //     //   text: 'Thanks for reaching out! We are looking into your issue.',
    //     //   timestamp: new Date(),
    //     // },
    //   ]);
    //   // can send email to support to notify
    // }, 1500);
  }

  closeTicket() {
    this.ticketService.closeTicket(this.id).subscribe(
      (res) => {
        this.loadData();
      },
      (err) => {
        console.log(err);
      }
    );
  }
}
