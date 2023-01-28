const API_PREFIX = '/api/v1';

const paginationParams = (
    page = 0,
    size = 20,
    sort = {field: 'createDate', order: 'ASC'}
) => `?page=${page}&size=${size}&sort=${sort.field},${sort.order}`;

export const CREATE_CERTIFICATE = API_PREFIX + '/certificates/creating';
export const certificatesUrl = (page, size, sort) =>
    API_PREFIX + '/certificates' + paginationParams(page, size, sort);
export const certificateByIdUrl = id => API_PREFIX + `/certificates/${id}`;

export const CATEGORIES = API_PREFIX + '/categories';

export const CREATE_PAYMENT = API_PREFIX + '/payments';
export const paymentByUserIdUrl = (id, page) =>
    API_PREFIX + `/users/${id}/payments?page=${page}&sort=createdDate,DESC`;
export const ordersByPaymentIdUrl = (id, page) =>
    API_PREFIX + `/payments/${id}/orders?page=${page}&size=10`;

export const LOGIN = API_PREFIX + '/users/login';
export const SIGNUP = API_PREFIX + '/users/signup';
