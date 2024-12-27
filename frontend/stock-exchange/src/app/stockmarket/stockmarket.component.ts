import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router'
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {StockmarketService} from "../service/stockmarket.service";

@Component({
  selector: 'stock-market',
  templateUrl: './stockmarket.component.html',
  styleUrls: ['./stockmarket.component.scss']
})
export class StockmarketComponent implements OnInit {

  name: string | undefined;
  token: string | undefined;
  stockExchanges: any[] = [];
  stocks: any
  errorMessage: string | undefined;
  stockExchangeForm!: FormGroup;
  updateStockForm!: FormGroup;
  stockForm!: FormGroup;
  noStocks: boolean = false;
  viewStockExchangeForm: boolean = false;
  viewAddStockForm: boolean = false;
  viewCreateStockForm: boolean = false;
  addStockForm!: FormGroup;
  stocksFromDb: any
  successMessage: string | undefined;

  constructor(private fb: FormBuilder, private router: Router, private stockMarketService: StockmarketService) {
    const navigation = this.router.getCurrentNavigation();
    if (navigation?.extras?.state) {
      const userData = navigation.extras.state['userData'];
      this.name = userData.name;
      this.token = userData.token;
    }
  }

  ngOnInit() {
    this.stockExchangeForm = this.fb.group({
      stockExchange: ['']
    });
    this.updateStockForm = this.fb.group({
      updatedPrice: ['']
    });
    this.stockForm = this.fb.group({
      stockName: ['', [Validators.required]],
      description: ['', Validators.required],
      price: ['', Validators.required]
    });
    this.addStockForm = this.fb.group({
      stockExchangeName: ['', [Validators.required]],
      stockId: ['', [Validators.required]]
    });
  }

  fetchStocks(event: Event): void {
    const selectedValue = (event.target as HTMLSelectElement).value;
    this.stockMarketService.getStocksFromAStockExchange(this.token, selectedValue).subscribe({
      next: data => {
        if (data.length == 0) {
          this.noStocks = true;
          this.stocks = [];
        } else {
          this.stocks = data;
          this.noStocks = false;
        }
      },
      error: error => {
        this.errorMessage = error.error.errorMessage;
        this.noStocks = true;
      }
    })
  }

  viewStockExchanges(): void {
    this.viewStockExchangeForm = true;
    this.viewAddStockForm = false;
    this.viewCreateStockForm = false;
    this.stockExchangeForm = this.fb.group({
      stockExchange: ['']
    });
    this.stocks = [];
    this.stockMarketService.getStockExchanges(this.token).subscribe({
      next: data => {
        this.stockExchanges = data;
      },
      error: error => {
        this.errorMessage = error.error.errorMessage;
      }
    })
  }

  viewStockForm(): void {
    this.viewAddStockForm = false;
    this.viewCreateStockForm = true;
    this.viewStockExchangeForm = false;
    this.stockForm.reset();
  }

  viewAddStockFormFunc(): void {
    this.viewAddStockForm = true;
    this.viewCreateStockForm = false;
    this.viewStockExchangeForm = false;
    this.addStockForm = this.fb.group({
      stockExchangeName: ['', [Validators.required]],
      stockId: ['', [Validators.required]]
    });
    this.stockMarketService.getStockExchanges(this.token).subscribe({
      next: data => {
        this.stockExchanges = data;
        this.stockMarketService.getStocks(this.token).subscribe({
          next: data => {
            this.stocksFromDb = data;
          },
          error: error => {
            this.errorMessage = error.error.errorMessage;
          }
        })
      },
      error: error => {
        this.errorMessage = error.error.errorMessage;
      }
    })
  }

  editStock(stock: any): void {
    stock.editable = !stock.editable;
    this.updateStockForm.patchValue({updatedPrice: stock.currentPrice});
  }

  updateStockPrice(stock: any): void {
    if (stock.editable) {
      this.stockMarketService.updateStockPrice(this.token, stock.id, this.updateStockForm.value.updatedPrice).subscribe({
        next: data => {
          stock.currentPrice = this.updateStockForm.value.updatedPrice;
          stock.editable = false;
          this.successMessage = 'Stock price updated successfully!';
          setTimeout(() => {
            this.successMessage = undefined;
          }, 2000);
        },
        error: error => {
          this.errorMessage = error.error?.errorMessage || 'Something went wrong. Please try again later.';
          stock.editable = false;
        }
      });
    } else {
      stock.editable = true;
    }
  }

  createStock(): void {
    this.stockMarketService.createStock(this.token, this.stockForm.value.stockName, this.stockForm.value.description, this.stockForm.value.price).subscribe({
      next: data => {
        this.stockForm.reset();
        this.successMessage = 'Stock has been created successfully!';
        setTimeout(() => {
          this.successMessage = undefined;
        }, 2000);
      },
      error: error => {
        this.errorMessage = error.error.errorMessage;
        this.stockForm.reset();
      }
    })
  }

  addStock(): void {
    this.stockMarketService.addStockToAStockExchange(this.token, this.addStockForm.value.stockExchangeName, this.addStockForm.value.stockId).subscribe({
      next: data => {
        this.addStockForm = this.fb.group({
          stockExchangeName: ['', [Validators.required]],
          stockId: ['', [Validators.required]]
        });
        this.successMessage = 'Stock has been added to the selected stock exchange successfully!';
        setTimeout(() => {
          this.successMessage = undefined;
        }, 2000);
      },
      error: error => {
        this.errorMessage = error.error?.errorMessage || 'Something went wrong. Please try again later.';
        this.addStockForm = this.fb.group({
          stockExchangeName: ['', [Validators.required]],
          stockId: ['', [Validators.required]]
        });
      }
    })
  }
}
