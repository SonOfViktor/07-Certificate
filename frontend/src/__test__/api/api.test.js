import * as api from '../../api/api';

describe('create url methods', () => {
  const API_PREFIX = '/api/v1';

  test('certificates url', () => {
    const actual = api.certificatesUrl(1, 50, {field: 'name', order: 'DESC'});
    const expected = API_PREFIX + '/certificates?page=1&size=50&sort=name,DESC';

    expect(actual).toBe(expected);
  });

  test('certificates url with default params', () => {
    const actual = api.certificatesUrl();
    const expected =
        API_PREFIX + '/certificates?page=0&size=20&sort=createDate,ASC';

    expect(actual).toBe(expected);
  });

  test('certificate by id url', () => {
    const actual = api.certificateByIdUrl(1);
    const expected = API_PREFIX + '/certificates/1';

    expect(actual).toBe(expected);
  });

  test('payment by user id url', () => {
    const actual = api.paymentByUserIdUrl(1, 4);
    const expected =
        API_PREFIX + '/users/1/payments?page=4&sort=createdDate,DESC';

    expect(actual).toBe(expected);
  });

  test('orders by payment id url', () => {
    const actual = api.ordersByPaymentIdUrl(1, 3);
    const expected = API_PREFIX + '/payments/1/orders?page=3&size=10';

    expect(actual).toBe(expected);
  });
});

