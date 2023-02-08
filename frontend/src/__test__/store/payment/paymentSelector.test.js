import {
  selectPayments,
  selectPaymentsMeta,
} from '../../../store/payment/paymentSelectors';

describe('payment selectors', () => {
  const paymentState = {
    status: 'received',
    payments: [{id: 1}, {id: 2}, {id: 3}],
    error: '',
    page: {number: 0, size: 20, totalElements: 0, totalPages: 0},
  };

  test('select payments', () => {
    expect(selectPayments({payments: paymentState})).toEqual(paymentState);
  });

  test('select payments meta', () => {
    expect(selectPaymentsMeta({payments: paymentState})).toEqual({
      status: 'received',
      error: '',
    });
  });
});
