import {Component} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {ActivatedRoute, NavigationEnd, Router, RouterEvent} from "@angular/router";
import { faChevronRight } from '@fortawesome/free-solid-svg-icons';
import {Order, Pizza} from "./types";
import { interval } from 'rxjs'

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent {
    constructor(private http: HttpClient, private route: ActivatedRoute, private router: Router) {

    }
    icons = {
        faChevronRight
    }

    title = 'client';
    orders: Order[] = [];
    pizzas: Pizza[] = [];

    // Display mode
    mode: string = '';

    ngOnInit() {
        this.refreshPizzasAndOrder();
        interval(1000)
            .subscribe(() => {
                this.refreshPizzasAndOrder();
            });
        // WTF because else component is loaded before routing so queryparams are empty
        this.router.events.subscribe(
            (event) => {
                if (event instanceof NavigationEnd) {
                    this.route.queryParamMap
                        .subscribe(params => {
                            let paramMode = params.get('mode');
                            if (paramMode) {
                                this.mode = paramMode;
                            } else {
                                this.router.navigate(
                                    [],
                                    {
                                        relativeTo: this.route,
                                        queryParams: { mode: 'pizza' },
                                        queryParamsHandling: 'merge', // remove to replace all query params by provided
                                    });
                            }
                        })
                }
            }
        );

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
            .subscribe((response: any) => {
                console.log(response);
                this.refreshPizzasAndOrder();
                this.printBarcode(response.id)
            })
    }

    ready(pizzaType: string) {
        this.http.post(`/${pizzaType}/ready`, {})
            .subscribe(() => {
                this.refreshPizzasAndOrder();
            })
    }

    onKey(event: KeyboardEvent) { // without type info
        if(event.key === 'Enter') {
            this.http.post(`/${(event.target as HTMLInputElement).value.trim()}/delivered`, {})
                .subscribe(() => {
                    this.refreshPizzasAndOrder();
                    (event.target as HTMLInputElement).value = '';
                })
        }
    }

    get sortedOrders(): Order[] {
        return this.orders
            .sort((a: Order, b: Order) => a.id > b.id ? 1 : a.id === b.id ? 0 : -1)
            .filter(o => o.status !== 'PICKED_UP')
            .reverse();
    }

    get preparingOrders(): Order[] {
        return this.orders
            .sort((a: Order, b: Order) => a.id > b.id ? 1 : a.id === b.id ? 0 : -1)
            .filter(o => o.status === 'ORDERED');
    }

    get readyOrders(): Order[] {
        return this.orders
            .sort((a: Order, b: Order) => a.id > b.id ? 1 : a.id === b.id ? 0 : -1)
            .filter(o => o.status === 'DELIVERED');
    }



    printBarcode(id: string | number) {
        const Pagelink = "about:blank";
        const pwa = window.open(Pagelink, "_new");
        pwa?.document.open();
        pwa?.document.write(`<html><head><script>function step1(){
setTimeout('step2()', 10);}
function step2(){window.print();window.close()}
</script></head><body onload='step1()'>
<img src='/barcode?id=${id}' /></body></html>`);
        pwa?.document.close();
    }

    goTo(dest: string) {
        this.router.navigate(
            [],
            {
                relativeTo: this.route,
                queryParams: { mode: dest },
                queryParamsHandling: 'merge', // remove to replace all query params by provided
            });
    }
}
