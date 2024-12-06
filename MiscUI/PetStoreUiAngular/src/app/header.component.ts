import { Component } from '@angular/core';
import { UserInfo } from './UserInfo';

@Component({
  selector: 'app-header',
  standalone: true,
  styleUrl: './header.component.css',
  templateUrl: './header.component.html',
})

export class HeaderComponent {
  user_info = new UserInfo("Test Pet Holder");
}
