import {StockmarketService} from "./stockmarket.service";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {TestBed} from "@angular/core/testing";

describe('StockmarketService', () => {
  let service: StockmarketService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [StockmarketService]
    });
    service = TestBed.inject(StockmarketService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call getStocksFromAStockExchange API with correct URL and headers', () => {
    const token = 'test-token';
    const stockExchangeName = 'NYSE';

    service.getStocksFromAStockExchange(token, stockExchangeName).subscribe();

    const req = httpMock.expectOne(`${service['stockExchangeBaseUrl']}/${stockExchangeName}`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${token}`);
  });

  it('should call getStockExchanges API with correct URL and headers', () => {
    const token = 'test-token';

    service.getStockExchanges(token).subscribe();

    const req = httpMock.expectOne(service['stockExchangeBaseUrl']);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${token}`);
  });

  it('should call getStocks API with correct URL and headers', () => {
    const token = 'test-token';

    service.getStocks(token).subscribe();

    const req = httpMock.expectOne(service['stockBaseUrl']);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${token}`);
  });

  it('should call updateStockPrice API with correct URL, payload, and headers', () => {
    const token = 'test-token';
    const stockId = '1';
    const updatedPrice = 100;

    service.updateStockPrice(token, stockId, updatedPrice).subscribe();

    const req = httpMock.expectOne(service['stockBaseUrl']);
    expect(req.request.method).toBe('PUT');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${token}`);
    expect(req.request.body).toEqual({ id: stockId, currentPrice: updatedPrice });
  });

  it('should call createStock API with correct URL, payload, and headers', () => {
    const token = 'test-token';
    const stockName = 'AAPL';
    const description = 'Apple Inc.';
    const stockPrice = 150;

    service.createStock(token, stockName, description, stockPrice).subscribe();

    const req = httpMock.expectOne(service['stockBaseUrl']);
    expect(req.request.method).toBe('POST');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${token}`);
    expect(req.request.body).toEqual({ name: stockName, description, currentPrice: stockPrice });
  });

  it('should call addStockToAStockExchange API with correct URL, payload, and headers', () => {
    const token = 'test-token';
    const stockExchangeName = 'NYSE';
    const stockId = '1';

    service.addStockToAStockExchange(token, stockExchangeName, stockId).subscribe();

    const req = httpMock.expectOne(`${service['stockExchangeBaseUrl']}/${stockExchangeName}`);
    expect(req.request.method).toBe('POST');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${token}`);
    expect(req.request.body).toEqual({ id: stockId });
  });
});
