import { Injectable } from '@angular/core';
import { FundBasket } from './FundBasket';
import { Toy } from './toy';
import { Observable, of, switchMap, forkJoin, map} from 'rxjs'; 
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { ToyService } from './toy.service';
import { EmailRequest } from './EmailRequest';


@Injectable({
  providedIn: 'root'
})
export class BasketService {

  //to snag the basket data from API
  private basketUrl = 'http://localhost:8080/baskets'

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient, private toyService: ToyService) { }

  /**
   * Gets the basket given an id from backend
   * @param id Id of basket to get  
   * @returns an Observable basket object
   */
  getBasket(id: number): Observable<FundBasket> {

    const url = `${this.basketUrl}/${id}`

    return this.http.get<FundBasket>(url);

  }

  /**
   * Creates a new basket
   * @returns The new basket
   */
  createBasket(): Observable<FundBasket> {
    return this.http.post<FundBasket>(this.basketUrl, {});
  }

  /**
   * Adds a toy to a given basket
   * @param basketId the id of basket to add toy to
   * @param toy the toy to add
   * @returns the udpated basket
   */
  addToyToBasket(basketId: number, toy: Toy): Observable<FundBasket> {

    const url = `${this.basketUrl}/${basketId}`

    return this.http.post<FundBasket>(url, toy, this.httpOptions);
  }

  /**
   * Removes a toy from the baskets basket
   * @param basketId the Id of basket to remove 
   * @param toyId The Id of toy to remove
   * @returns an Http response of 200, 404 if basket or toy not found
   */
  removeToy(basketId: number, toyId:number): Observable<FundBasket> {

    const url = `${this.basketUrl}/${basketId}/toys/${toyId}`

    return this.http.delete<any>(url, this.httpOptions)
  }

  /**
   * Checkouts the current basket, updating the Toy and basket storage
   * @param basket the basket to checkout
   * @returns the updated empty basket
   */
  checkout(basket: FundBasket): Observable<any> {

    const observables: Observable<any>[] = [];

    // Create observables for updating and removing toys
    basket.basket.forEach(item => {
      const updateToy$ = this.toyService.getToy(item.id).pipe(
        switchMap(toy => {
          const updatedToy: Toy = { ...toy, quantity: toy.quantity - item.quantity };
          return this.toyService.updateToy(updatedToy);
        })
      );
  
      const removeToy$ = this.removeToy(basket.id, item.id);
  
      observables.push(updateToy$, removeToy$);
    });
  
    // Combine all observables using forkJoin
    return forkJoin(observables).pipe(
      // After all operations are completed, return the result of getbasket
      switchMap(() => this.getBasket(basket.id))
    );
  }

  /**
   * Gets all the baskets from backend
   * @returns an observable list of all baskets
   */
  getAllBaskets(): Observable<FundBasket[]> {

    return this.http.get<FundBasket[]>(this.basketUrl);
    
  }

  /**
   * Sends a request to the backend to send an email receipt
   * @param emailRequest the email to be sent
   * @returns an observable containing a http.OK or http.internalServerError
   */
  sendReceipt(emailRequest: EmailRequest): Observable<any> {

    const url = `${this.basketUrl}/send-email`;

    console.log(emailRequest);
    
    return this.http.post<any>(url, emailRequest,this.httpOptions);
  }
  
}
