import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { UserInfo } from './UserInfo';
import { HeaderComponent } from './header.component';
import {PetsComponent} from './pets/pets.component';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [HeaderComponent, PetsComponent, HttpClientModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})

export class AppComponent {
  user_info = new UserInfo("test");
}
