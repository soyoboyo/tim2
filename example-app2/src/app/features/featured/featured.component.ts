import {Component, OnInit} from '@angular/core';

interface Products {
  productContent: string,
  imgURL: string,
  alt: string
}

@Component({
  selector: 'app-featured',
  templateUrl: './featured.component.html',
  styleUrls: ['./featured.component.scss']
})
export class FeaturedComponent implements OnInit {

  products: Products[] = [
    {
      productContent: 'productContent1',
      imgURL: 'https://cdn.pixabay.com/photo/2016/11/29/13/39/analog-watch-1869928_960_720.jpg',
      alt: 'Watch'
    },
    {
      productContent: 'productContent2',
      imgURL: 'https://cdn.pixabay.com/photo/2016/11/19/15/50/chair-1840011_960_720.jpg',
      alt: 'Chair'
    },
    {
      productContent: 'productContent3',
      imgURL: 'https://cdn.pixabay.com/photo/2017/04/04/18/12/video-game-console-2202613_960_720.jpg',
      alt: 'Gameboy'
    },
  ]

  constructor() {
  }

  ngOnInit(): void {
  }

}
