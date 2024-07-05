import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../user.service';
import { HttpClient } from '@angular/common/http';
import { User } from '../user';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  user!: User;

  username!: string;

  errorMessage: string | null = null;

  constructor(private router: Router, private userService: UserService, private http: HttpClient) {}

  login() {

    if (!this.username) {
      this.errorMessage = 'Please enter a username.';
      return;
    }
    
    this.userService.login(this.username).subscribe(
      user => {
        console.log(user);
        this.router.navigate(['/toys'], {queryParams: {id: user.id.toString(), isPartner: user.isPartner}});
      },
      error => {
        console.error('Error fetching user ID', error);
      }
    );
  }
}
