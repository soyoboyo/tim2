import {Component, OnInit} from '@angular/core';
import {LangChangeEvent, TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.scss']
})
export class CategoriesComponent implements OnInit {

  categories = []

  constructor(private translate: TranslateService) {
    translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.translate.get(['categoryHome', 'categoryElectronics', 'categoryVideoGames'])
        .subscribe(translations => {
          this.categories = [
            translations.categoryHome, translations.categoryElectronics, translations.categoryVideoGames
          ]
        })
    });
  }

  ngOnInit(): void {
  }

}
