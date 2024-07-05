import { Component, OnInit } from '@angular/core';
import { Toy } from '../toy';
import { FundBasket } from '../FundBasket';
import { BasketService } from '../basket.service';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { Location } from '@angular/common';
import { EmailRequest } from '../EmailRequest';


@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrls: ['./basket.component.css']
})
export class BasketComponent implements OnInit {

  fundBasket!: FundBasket;

  totalAmount: number = 0;

  receiptBody: string = '';

  userEmail: string = '';

  userId!: number;

  constructor(
    private basketService: BasketService,
    private route: ActivatedRoute,
    private location: Location,
    private router: Router
  ) {}

  ngOnInit(): void {
      this.getBasket();
  }

  /**
   * Gets the user's basket
   * @param id id of user's basket
   */
  getBasket(): void {

    //set the user id
    this.userId = Number(this.route.snapshot.paramMap.get('id'));

    this.basketService.getBasket(this.userId)
    .subscribe(basket => {
      this.fundBasket = basket;
      // Calculate total amount
      this.calculateTotalAmount();
      this.generateReceiptBody();
    });
  }

  /**
   * Calculates the total $ in the basket
   */
  calculateTotalAmount(): void {
    this.totalAmount = this.fundBasket.basket.reduce((total, item) => 
    total + (item.cost * item.quantity), 0);
  }

  generateReceiptBody(): void {
    this.receiptBody = `Thank you for your purchase!\n\n`;
    this.fundBasket.basket.forEach((item, index) => {
      this.receiptBody += `${index + 1}. ${item.name}: $${item.cost} x ${item.quantity}\n`;
    });
    this.receiptBody += `\nTotal Amount: $${this.totalAmount}`;
  }

  /**
   * Removes a Toy from the basket, binded to a remove button
   * @param basketId the id of the basket 
   * @param toyId the id of the toy to remove
   */
  removeToy(id: number, toyId: number): void {

    this.basketService.removeToy(id, toyId).subscribe(() => {

      //update the basket
      this.getBasket();

    })
  }

  /**
   * Returns to home screen
   */
  goBack(): void {

    this.router.navigate(['/toys'], { queryParams: { id: this.userId } });
  }

  /**
   * Calls the checkout fcn within the basket, updating both the basket and toys
   * @param basket the basket to checkout with
   */
  checkout(basket: FundBasket): void {

    if (this.userEmail != '') {
      this.emailReceipt();
    }
    
    this.basketService.checkout(basket).subscribe(() => {
      //make sure we get the most up to date version
      this.getBasket();
    });

  }

  /**
   * Sends an email to the user with inputted email
   */
  emailReceipt() {
    
    const emailRequest: EmailRequest = {
      toEmail: this.userEmail,
      body: this.receiptBody
    }

    this.basketService.sendReceipt(emailRequest).subscribe(() =>
    {
      console.log("email sent")
    });
  }

}
