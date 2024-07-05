import { Injectable } from '@angular/core';
import { User } from './user';
import { Observable, of} from 'rxjs'; 
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { switchMap, map } from 'rxjs/operators';
import { BasketService } from './basket.service';


@Injectable({
  providedIn: 'root'
})
export class UserService {

  

  private userUrl = 'http://localhost:8080/users';
  private curlEndpoint = 'http://localhost:8080/execute-curl'; 

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient, private basketService: BasketService) { }

  /**
   * Gets all users from Rest API
   * @returns An Observable object of list of Users
   */
  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.userUrl);
  }

  getApplicants(): Observable<number[]> {
    // This method will make a GET request to the `/users/applications` endpoint
    // and expects to receive an array of applicant IDs in response
    return this.http.get<number[]>(`${this.userUrl}/applications`);
  }
  
  /**
   * Calls the get/id from Rest API
   * @param id the id of specific user
   * @returns An Observable User object
   */
  getUserID(id: number): Observable<User> {

    const url = `${this.userUrl}/${id}`

    return this.http.get<User>(url);
  }

  /**
   * PUT to update a user
   * @param user the user to be updated
   * @returns the updated user
   */
  updateUser(user: User): Observable<any> {
    return this.http.put(this.userUrl, user, this.httpOptions);
  }

  /**
   * POST a new user
   * @param user the new user to create
   * @returns the new user
   */
  addUser(user: User): Observable<User> {
    return this.http.post<User>(this.userUrl, user, this.httpOptions);
  } 

  /**
   * Removes a user given a specific id 
   * @param id the id of user to remove
   * @returns true if deleted / false if not found
   */
  deleteUser(id:number): Observable<User> {

    const url = `${this.userUrl}/${id}`;

    return this.http.delete<User>(url, this.httpOptions);
  }

  /**
   * Searches user database for given user
   * @param term the string of the name searching for
   * @returns An observable user list object
   */
  searchUsers(term:string): Observable<User[]> {

    if(!term.trim()) {
      //if not search term return empty array
      return of([]);
    }
    return this.http.get<User[]>(`${this.userUrl}/?name=${term}`)
  }

  /**
   * Gets the ID of a user by their name
   * 
   * @param name The name of the user
   * @returns The ID of the user if found, or -1 if not found
   */
  getUserIdByName(name: string): Observable<number> {
    return this.http.get<number>(`${this.userUrl}/id/${name}`);
  }

  /**
   * Execute a cURL command on the server
   * @param command The cURL command to execute
   * @returns Observable<any> representing the response from the server
   */
  executeCurlCommand(command: string): Observable<any> {
    const data = { command };
    return this.http.post<any>(this.curlEndpoint, data, this.httpOptions);
  }

  /**
 * Logs in a user by their username
 * @param username The username of the user
 * @returns An Observable representing the user
 */
  login(username: string): Observable<User> {
    
    if(username != null){

      return this.getUserIdByName(username).pipe(
        switchMap(userId => {
          if (userId !== -1) {
            // User exists, fetch user details
            return this.getUserID(userId);
          } else {
            // User does not exist, create a new user
            const userToAdd: User = {
              name: username, id: 0,
              isPartner: false
            }; // Set a temporary ID, as the server will generate the actual ID
            return this.addUser(userToAdd).pipe(
              switchMap(newUser => {
                // Create a basket for the new user
                return this.basketService.createBasket().pipe(
                  // Return the new user after creating the basket
                  map(() => newUser)
                );
              })
            );
          }
        })
      );
  }
  else{
    return this.getUserID(-1);
  }
}

  /**
   * Check if a user is a partner by their ID
   * @param id The ID of the user
   * @returns An Observable<boolean> indicating if the user is a partner
   */
  isUserAPartner(id: number): Observable<boolean> {
    return this.getUserID(id).pipe(
      map(user => user.isPartner)
    );
  }

  makeUserPartner(id: number): Observable<any> {
    console.log(`Attempting to make user with ID ${id} a partner going to controller.`);
    return this.http.post(`${this.userUrl}/${id}/makePartner`, this.httpOptions);
  }

  applyForPartnership(userId: number): Observable<any> {
    return this.http.post(`${this.userUrl}/${userId}/apply-partnership`, this.httpOptions);
  }

  getPartners(): Observable<number[]> {
    return this.http.get<number[]>(`${this.userUrl}/partners`);
  }
}
