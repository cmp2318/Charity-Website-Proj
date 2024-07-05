import { Component } from '@angular/core';
import { Router } from '@angular/router';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Smiles Unlimited';

  constructor(private router: Router) {}

  /**
   * Method to check active route
   */

  isLoginPage(): boolean {
    return this.router.url === '/login';
  }

}
