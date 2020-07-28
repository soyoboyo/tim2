import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})
export class ProductComponent implements OnInit {
  @Input()
  productContent: string;
  @Input()
  imgURL: string;
  @Input()
  alt: string;

  constructor() {
  }

  ngOnInit(): void {
  }

}
