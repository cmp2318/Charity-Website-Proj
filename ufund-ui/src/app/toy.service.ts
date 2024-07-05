import { Injectable } from '@angular/core';
import { Toy } from './toy';
import { Observable, of} from 'rxjs'; 
import {HttpClient, HttpHeaders} from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class ToyService {

  private toysUrl = 'http://localhost:8080/toys';

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) { }

  /**
   * Gets all toys from Rest API
   * @returns An Observable object of list of Toys
   */
  getToys(): Observable<Toy[]> {
    return this.http.get<Toy[]>(this.toysUrl);
  }
  
  /**
   * Calls the get/id from Rest API
   * @param id the id of specific toy
   * @returns An Observable Toy object
   */
  getToy(id: number): Observable<Toy> {

    const url = `${this.toysUrl}/${id}`

    return this.http.get<Toy>(url);
  }

  /**
   * PUT to update a toy
   * @param toy the toy to be updated
   * @returns the updated toy
   */
  updateToy(toy: Toy): Observable<any> {
    return this.http.put(this.toysUrl, toy, this.httpOptions);
  }

  /**
   * POST a new toy
   * @param toy the new toy to create
   * @returns the new toy
   */
  addToy(toy: Toy): Observable<Toy> {

    return this.http.post<Toy>(this.toysUrl, toy, this.httpOptions);
  } 

  /**
   * Removes a toy given a specific id 
   * @param id the id of toy to remove
   * @returns true if deleted / false if not found
   */
  deleteToy(id:number): Observable<Toy> {

    const url = `${this.toysUrl}/${id}`;

    return this.http.delete<Toy>(url, this.httpOptions);
  }

  /**
   * Searches toy database for given toy
   * @param term the string of the name searching for
   * @returns An observable Toy list object
   */
  searchToys(term:string): Observable<Toy[]> {

    if(!term.trim()) {
      //if not search term return empty array
      return of([]);
    }
    return this.http.get<Toy[]>(`${this.toysUrl}/?name=${term}`)
  }

}
