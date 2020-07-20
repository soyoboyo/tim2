import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.scss']
})
export class CategoriesComponent implements OnInit {

  categories = [
    'Home',
    'Electronics',
    'Video Games'
  ]

  constructor() {
  }

  ngOnInit(): void {
  }

}
