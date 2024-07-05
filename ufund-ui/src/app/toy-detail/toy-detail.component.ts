import { Component, Input, OnInit } from '@angular/core';
import { Toy } from '../toy';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { ToyService } from '../toy.service';
import { BasketService } from '../basket.service';

@Component({
  selector: 'app-toy-detail',
  templateUrl: './toy-detail.component.html',
  styleUrls: ['./toy-detail.component.css']
})
export class ToyDetailComponent implements OnInit {

  @Input() toy?: Toy;
  userId!: number;

  constructor(
    private route: ActivatedRoute,
    private toyService: ToyService,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.getToy();
    this.getUserId();
  }

  getToy(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.toyService.getToy(id).subscribe(toy => this.toy = toy);
  }

  getUserId(): void {
    this.userId = Number(this.route.snapshot.paramMap.get('userId'));
  }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    if (this.toy && this.toy.quantity >= 0 && this.toy.cost >= 0 ) {
      this.toyService.updateToy(this.toy).subscribe(() => this.goBack());
    }
  }

  remove(): void {
    if (this.toy) {
      this.toyService.deleteToy(this.toy.id).subscribe(() => this.goBack());
    }
  }
}