import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service'; // Adjust the path as necessary
import { Location } from '@angular/common';

interface Applicant {
  id: number;
  name: string;
}

@Component({
  selector: 'app-partnership',
  templateUrl: './partnership.component.html',
  styleUrls: ['./partnership.component.css']
})
export class PartnershipComponent implements OnInit {
  applications: Applicant[] = []; 

  constructor(
    private userService: UserService, 
    private location: Location
  ) { }

  ngOnInit(): void {
    this.fetchApplicants();
  }

  fetchApplicants(): void {
    this.userService.getApplicants().subscribe(applicantIds => {
      applicantIds.forEach(applicantId => {
        this.userService.getUserID(applicantId).subscribe(user => {
          // Push an object with both ID and name into the applications array
          this.applications.push({ id: user.id, name: user.name });
        });
      });
    });
}

acceptApplication(applicantId: number): void {
    console.log("Attempting to make user with ID a partner.");
    // Placeholder for calling a service method to accept the application
    this.userService.makeUserPartner(applicantId).subscribe(
      () => {
        console.log('Application accepted successfully for user ID:', applicantId);
        // Refresh the applications list or update the UI as needed
        window.location.reload();
      }
    );
  }

  /**
 * Returns to home screen
 */
  goBack(): void {
  this.location.back();
  } 

  findUser(applicantId: number): void{
  }



}