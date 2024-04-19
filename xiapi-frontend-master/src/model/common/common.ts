export interface Page{
    current: number
    pageSize: number
}

export interface Result<T>{
    code: number;
    data: T;
    message: string
}

export interface Data<T>{
    current: number;
    records: T;
    total: number
}
