import {Component, OnInit} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  translate: TranslateService;
  languages = [
    {value: 'en_US', viewValue: 'English'},
    {value: 'pl_PL', viewValue: 'Polish'}
  ]

  constructor(translate: TranslateService) {
    this.translate = translate;
  }

  ngOnInit(): void {
  }

  changeLanguage(event) {
    if (event.isUserInput) {
      this.translate.use(event.source.value);
      console.log(event.source.value)
    }
  }

}
