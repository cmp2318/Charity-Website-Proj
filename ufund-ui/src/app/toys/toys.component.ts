import { Component, OnInit } from '@angular/core';
import { Toy } from '../toy';
import { ToyService } from '../toy.service';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../user.service';


@Component({
  selector: 'app-toys',
  templateUrl: './toys.component.html',
  styleUrls: ['./toys.component.css']
})
export class ToysComponent implements OnInit {

  toys: Toy[] = [];
  userId: string | undefined;
  isPartner: boolean = false
  currentToys: Toy[] = [];
  currentPage: number = 1;
  itemsPerPage: number = 10;
  totalPages: number = 0;

  selectedToy?: Toy;

  errorMessage: string | null = null;

  //calls the RestAPI
  constructor(private toyService: ToyService,
    private route: ActivatedRoute, private userService: UserService) {}

  /**
   * Gets all the toys
   */
  getToys(): void {
    this.toyService.getToys()
      .subscribe(toys => {
        this.toys = toys;
        this.totalPages = Math.ceil(this.toys.length / this.itemsPerPage); 
        this.pageToys(); 
      });
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.userId = params['id'];
      this.checkIfUserIsPartner();
      this.getToys();
    });
  }

  checkIfUserIsPartner(): void {
    this.route.queryParams.subscribe(params => {
      this.userId = params['id'];
      if (this.userId) {
        const currentUserId = parseInt(this.userId, 10); 
        if (!isNaN(currentUserId)) { 
          this.userService.getPartners().subscribe(partnerIds => {
            this.isPartner = partnerIds.includes(currentUserId); 
            console.log('Is current user a partner:', this.isPartner);
          });
        }
      }
    });
  }

  /**
   * When toy selected, updates selected toy
   * @param toy the toy to select
   */
  onSelect(toy: Toy) {
    this.selectedToy = toy;
  }

  addToy(name: string, cost: string, 
    quantity: string, type: string): void {

      //check if the all fields are filled out
      if (!name || !cost || !quantity || !type) {
        this.errorMessage = 'Please fill out all fields';
        return;
      }

    const quanNum = parseInt(quantity);
    const costNum = parseInt(cost);

    // Check if cost and quantity are positive
    if (quanNum <= 0) {
      this.errorMessage = 'Quantity cannot be negative';
      return;
    }

    if (costNum < 0) {
      this.errorMessage = 'Cost cannot be negative';
      return;
    }

    const newToy: Toy = {id: 0, name: name, 
      quantity: quanNum, cost: costNum, type: type};

    this.toyService.addToy(newToy).subscribe(
      (toy: Toy) => {
        console.log("Toy added: ", toy);

        window.location.reload();
        
      }
    );
      
  }

  applyForPartnership(): void {
    if (this.userId) {
      this.userService.applyForPartnership(+this.userId).subscribe({
        next: () => alert("Applied! Please wait for admin to approve."),
        error: (error) => alert("You already applied")
      });
    }
  }

  // Goes to next page of toys
  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.pageToys();
    }
  }
  
  previousPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.pageToys();
    }
  }

  // Puts the right toys on the page
  pageToys() {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.currentToys = this.toys.slice(startIndex, endIndex);
  }
}
