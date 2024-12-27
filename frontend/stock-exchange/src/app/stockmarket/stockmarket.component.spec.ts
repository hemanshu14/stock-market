import {ComponentFixture, TestBed} from '@angular/core/testing';
import {Router} from '@angular/router';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {RouterTestingModule} from '@angular/router/testing';
import {By} from '@angular/platform-browser';
import {StockmarketComponent} from "../stockmarket/stockmarket.component";

describe('StockmarketComponent', () => {
  let component: StockmarketComponent;
  let fixture: ComponentFixture<StockmarketComponent>;
  let httpMock: HttpTestingController;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [StockmarketComponent],
      imports: [
        ReactiveFormsModule,
        HttpClientTestingModule,
        RouterTestingModule
      ],
      providers: []
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StockmarketComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have a form with stockName, description, and price fields on click of Create Stock button', () => {
    jest.spyOn(component, 'viewStockForm');
    const button = fixture.debugElement.query(By.css('#viewCreateStock'));
    button.triggerEventHandler('click', null);
    fixture.detectChanges();
    expect(component.viewStockForm).toHaveBeenCalled();
    const stockNameInput = fixture.debugElement.query(By.css('input[formControlName="stockName"]'));
    const descriptionInput = fixture.debugElement.query(By.css('input[formControlName="description"]'));
    const priceInput = fixture.debugElement.query(By.css('input[formControlName="price"]'));
    expect(stockNameInput).toBeTruthy();
    expect(descriptionInput).toBeTruthy();
    expect(priceInput).toBeTruthy();
  });

  it('should create a stock on click of Create Stock button by calling the api', () => {
    component.stockForm.setValue({stockName: 'Apple', description: 'Apple co.', price: 23.8});
    component.createStock();
    const req = httpMock.expectOne('http://localhost:8005/api/v1/stocks');
    req.flush('Stock has been created');

    expect(component.successMessage).toEqual('Stock has been created successfully!');
  });

  it('should not create a stock if the api gives an error', () => {
    component.stockForm.setValue({stockName: 'Apple', description: 'Apple co.', price: 23.8});
    component.createStock();
    const req = httpMock.expectOne('http://localhost:8005/api/v1/stocks');
    req.flush({errorMessage: 'Something went wrong. Please try again later.'}, {
      status: 500,
      statusText: 'Service Temporarily unavailable'
    });
    expect(component.errorMessage).toEqual('Something went wrong. Please try again later.');
  });

  it('should have a form with stockExchange field with stock exchange names on click of View Stocks button', () => {
    jest.spyOn(component, 'viewStockExchanges');
    const button = fixture.debugElement.query(By.css('#viewStocks'));
    button.triggerEventHandler('click', null);
    fixture.detectChanges();
    expect(component.viewStockExchanges).toHaveBeenCalled();
    const req = httpMock.expectOne('http://localhost:8005/api/v1/stock-exchanges');
    req.flush([
      {
        "id": 1,
        "name": "NASDAQ",
        "description": "NASDAQ",
        "liveInMarket": true
      }
    ]);
    const stockExchangeDropdown = fixture.debugElement.query(By.css('select[formControlName="stockExchange"]'));
    expect(stockExchangeDropdown).toBeTruthy();
    expect(component.stockExchanges.length).toEqual(1);
  });

  it('should not have a form with stockExchange field with stock exchange names on click of View Stocks button in case of any api failure', () => {
    jest.spyOn(component, 'viewStockExchanges');
    const button = fixture.debugElement.query(By.css('#viewStocks'));
    button.triggerEventHandler('click', null);
    fixture.detectChanges();
    expect(component.viewStockExchanges).toHaveBeenCalled();
    const req = httpMock.expectOne('http://localhost:8005/api/v1/stock-exchanges');
    req.flush({errorMessage: 'Something went wrong. Please try again later.'}, {
      status: 500,
      statusText: 'Service Temporarily unavailable'
    });

    fixture.detectChanges();
    const errorMessage = fixture.debugElement.query(By.css('.txt-error'));
    expect(errorMessage).toBeTruthy();
    if (errorMessage) {
      expect(errorMessage.nativeElement.textContent).toContain('Something went wrong. Please try again later.');
    }
  });

  it('should have a form with stockExchangeName, stockId on click of Add Stock button', () => {
    jest.spyOn(component, 'viewAddStockFormFunc');
    const button = fixture.debugElement.query(By.css('#viewAddStock'));
    button.triggerEventHandler('click', null);
    fixture.detectChanges();
    expect(component.viewAddStockFormFunc).toHaveBeenCalled();

    const req = httpMock.expectOne('http://localhost:8005/api/v1/stock-exchanges');
    req.flush([
      {
        "id": 1,
        "name": "NASDAQ",
        "description": "NASDAQ",
        "liveInMarket": true
      }
    ]);
    const stockReq = httpMock.expectOne('http://localhost:8005/api/v1/stocks');
    stockReq.flush([
      {
        "id": 1,
        "name": "Apple",
        "description": "Apple co."
      }
    ]);
    expect(component.stockExchanges.length).toEqual(1);
    expect(component.stocksFromDb.length).toEqual(1);
    const stockExchangeName = fixture.debugElement.query(By.css('select[formControlName="stockExchangeName"]'));
    const stock = fixture.debugElement.query(By.css('select[formControlName="stockId"]'));
    expect(stockExchangeName).toBeTruthy();
    expect(stock).toBeTruthy();
  });

  it('should add a stock to the selected stock exchange on click of Add Stock button by calling the api', () => {
    component.addStockForm.setValue({stockExchangeName: 'NASDAQ', stockId: '1'});
    component.addStock();
    const req = httpMock.expectOne(`http://localhost:8005/api/v1/stock-exchanges/${component.addStockForm.value.stockExchangeName}`);
    req.flush('Stock has been added');

    expect(component.successMessage).toEqual('Stock has been added to the selected stock exchange successfully!');
  });

  it('should not add a stock to the selected stock exchange if the api gives an error', () => {
    component.addStockForm.setValue({stockExchangeName: 'NASDAQ', stockId: '1'});
    component.addStock();
    const req = httpMock.expectOne(`http://localhost:8005/api/v1/stock-exchanges/${component.addStockForm.value.stockExchangeName}`);

    req.flush({errorMessage: 'Something went wrong. Please try again later.'}, {
      status: 500,
      statusText: 'Service Temporarily unavailable'
    });
    fixture.detectChanges();
    expect(component.errorMessage).toEqual('Something went wrong. Please try again later.');
  });

  it('should updated the price of the selected stock on click of update button', () => {
    component.updateStockForm.setValue({updatedPrice: 29.8});
    const stock = {
      id: 1,
      editable: true
    };
    component.updateStockPrice(stock);

    const req = httpMock.expectOne('http://localhost:8005/api/v1/stocks');
    req.flush('Stock price has been updated');

    expect(component.successMessage).toEqual('Stock price updated successfully!');
  });

  it('should not update stock price if the api gives an error', () => {
    component.updateStockForm.setValue({updatedPrice: 29.8});
    const stock = {
      id: 1,
      editable: true
    };
    component.updateStockPrice(stock);

    const req = httpMock.expectOne('http://localhost:8005/api/v1/stocks');
    req.flush({errorMessage: 'Something went wrong. Please try again later.'}, {
      status: 500,
      statusText: 'Service Temporarily unavailable'
    });
    expect(component.errorMessage).toEqual('Something went wrong. Please try again later.');
  });
});
