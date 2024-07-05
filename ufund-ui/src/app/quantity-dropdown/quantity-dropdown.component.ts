import { Component, OnInit, Input } from '@angular/core';
import { Toy } from '../toy';
import { BasketService } from '../basket.service';
import { ToyService } from '../toy.service';
import { FundBasket } from '../FundBasket';
import { Router } from '@angular/router';

@Component({
  selector: 'app-quantity-dropdown',
  templateUrl: './quantity-dropdown.component.html',
  styleUrls: ['./quantity-dropdown.component.css']
})
export class QuantityDropdownComponent implements OnInit  {

  @Input() toy!: Toy; //current toy

  @Input() userId!: number;

  maxQuantity!: number; // max quantity in dropdown

  selectedQuantity: number | undefined = 1 // the selected quantity

  dropdownOptions: number[] = [];

  constructor 
  (private toyService: ToyService,
  private basketService: BasketService,
  private router: Router) {}

  
  ngOnInit(): void {
      this.calcMaxQuantity();
  }

  /**
   * Calculates the max amount you can add to basket
   */
  calcMaxQuantity(): void {

    this.basketService.getBasket(this.userId).subscribe((basket: FundBasket) => {
      this.toyService.getToy(this.toy.id).subscribe((toyFromServer: Toy) => {
        const toyInBasket = basket.basket.find(t => t.id === this.toy.id);
        if (!toyInBasket) {
          this.maxQuantity = toyFromServer.quantity; // If toy not in basket, set max quantity to stock
        } else {
          this.maxQuantity = Math.min(toyFromServer.quantity - toyInBasket.quantity, toyFromServer.quantity); // Set max quantity to minimum of remaining stock and total stock
        }

        this.genDropdownOptions();
      });
    });

  }

  genDropdownOptions(): void {
    this.dropdownOptions = [];
    for (let i = 1; i <= this.maxQuantity; i++) {
      this.dropdownOptions.push(i);
    }
  }

  /**
   * When a quantity is selected, updates selected quantity
   * @param event the quantity to update
   */
  onSelectQuantity(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.selectedQuantity = value !== '' ? +value : undefined; // Use a default value, in this case 1
  }
  
  
  /**
   * Adds a toy to the basket with given amount
   */
  addToBasket(): void {
    if (this.toy && this.selectedQuantity) {
      const toyToAdd: Toy = { ...this.toy }; // Create a copy of the toy object
      toyToAdd.quantity = this.selectedQuantity; // Set the quantity to add
      this.basketService.addToyToBasket(this.userId, toyToAdd)
        .subscribe(() => {
          console.log('Toy added to basket');
          this.router.navigate([`/baskets/${this.userId}`]);
        });
    }
  }
}
