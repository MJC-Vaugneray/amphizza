export type Order = {
    id: number,
    pizzaType: string,
    status: string,
    createdAt: string
}

export type Pizza = {
    id: number,
    pizzaType: string,
    doingNumber: number,
    readyNumber: number
}