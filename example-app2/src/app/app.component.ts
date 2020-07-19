import {Component} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'example-app2';

  constructor(translate: TranslateService) {
    translate.setDefaultLang('en_US');

    translate.use('pl_PL');
  }
}
