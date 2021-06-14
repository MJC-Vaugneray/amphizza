import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor(  private http: HttpClient) {

  }

  title = 'client';
  orders: any;
  pizzas: any;
  ngOnInit() {
    this.refreshPizzasAndOrder();
  }

  private refreshPizzasAndOrder() {
    this.http.get<any[]>('/orders')
        .subscribe(orders => {
          this.orders = orders;
        })

    this.http.get<any[]>('/pizzas')
        .subscribe(pizzas => {
          this.pizzas = pizzas;
        })
  }

  order(pizzaType: string) {
    this.http.post(`/${pizzaType}/new`, {})
        .subscribe(() => {
          this.refreshPizzasAndOrder();
        })
  }

  ready(pizzaType: string) {
    this.http.post(`/${pizzaType}/ready`, {})
        .subscribe(() => {
          this.refreshPizzasAndOrder();
        })
  }

  delivered(pizzaType: string) {
    this.http.post(`/${pizzaType}/delivered`, {})
        .subscribe(() => {
          this.refreshPizzasAndOrder();
        })
  }
}
