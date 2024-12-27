import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StockmarketService {
  private stockBaseUrl = 'http://localhost:8005/api/v1/stocks';
  private stockExchangeBaseUrl = 'http://localhost:8005/api/v1/stock-exchanges';

  constructor(private http: HttpClient) {
  }

  getStocksFromAStockExchange(token: string | undefined, stockExchangeName: string): Observable<any> {
    return this.http.get<any>(`${this.stockExchangeBaseUrl}/${stockExchangeName}`, {
      headers: {'Authorization': `Bearer ${token}`}
    });
  }

  getStockExchanges(token: string | undefined): Observable<any> {
    return this.http.get<any>(`${this.stockExchangeBaseUrl}`, {
      headers: {'Authorization': `Bearer ${token}`}
    });
  }

  getStocks(token: string | undefined): Observable<any> {
    return this.http.get<any>(`${this.stockBaseUrl}`, {
      headers: {'Authorization': `Bearer ${token}`}
    });
  }

  updateStockPrice(token: string | undefined, stockId: string, updatedPrice: any): Observable<any> {
    return this.http.put<any>(`${this.stockBaseUrl}`, {
      'id': stockId,
      'currentPrice': updatedPrice
    }, {
      headers: {'Authorization': `Bearer ${token}`}, responseType: 'text' as 'json'
    });
  }

  createStock(token: string | undefined, stockName: string, description: string, stockPrice: any): Observable<any> {
    return this.http.post<any>(`${this.stockBaseUrl}`, {
      'name': stockName,
      'description': description,
      'currentPrice': stockPrice
    }, {
      headers: {'Authorization': `Bearer ${token}`}
    });
  }

  addStockToAStockExchange(token: string | undefined, stockExchangeName: string, stockId: any): Observable<any> {
    return this.http.post<any>(`${this.stockExchangeBaseUrl}/${stockExchangeName}`, {
      'id': stockId
    }, {
      headers: {'Authorization': `Bearer ${token}`}, responseType: 'text' as 'json'
    });
  }
}
