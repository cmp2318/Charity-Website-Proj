import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../user.service';
import { BasketService } from '../basket.service';
import { FundBasket } from '../FundBasket';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  username: string | undefined;
  id: number | undefined;
  basket: FundBasket | undefined;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private basketService: BasketService
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.username = params['username'];
      if (!this.username) {
        console.error('Username is undefined');
        return; // Exit if username is undefined
      }
  
      this.userService.getUserIdByName(this.username).subscribe(
        userId => {
          this.id = userId;
          if (!this.id) {
            console.error('User ID is undefined');
            return; // Exit if user ID is undefined
          }
          this.fetchUserBasket();
        },
        error => {
          console.error('Error fetching user ID:', error);
        }
      );
    });
  }

  private fetchUserBasket(): void {
    this.basketService.getBasket(this.id!).subscribe(
      basket => {
        this.basket = basket;
      },
      error => {
        console.error('Error fetching user basket:', error);
      }
    );
  }
}