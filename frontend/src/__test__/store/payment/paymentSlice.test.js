import axios from 'axios';
import {
  createPayment,
  loadOrders,
  loadPayments,
  paymentReducer,
  resetPaymentStatus,
} from '../../../store/payment/paymentSlice';

jest.mock('axios');

describe('payments async actions', () => {
  const dispatch = jest.fn();

  beforeAll(() => {
    localStorage.setItem('token', 'someusertoken');
  });

  test('create payment action', async () => {
    axios.post.mockReturnValue({paymentId: 10});

    const thunk = createPayment();
    await thunk(dispatch, null, {client: axios, api: {}});
    const [pending, fulfilled] = dispatch.mock.calls;
    const authHeader = axios.post.mock.calls[0][2].headers.Authorization;

    expect(authHeader).toBe('Bearer someusertoken');
    expect(pending[0].type).toBe(createPayment.pending().type);
    expect(fulfilled[0].type).toBe(createPayment.fulfilled().type);
    expect(fulfilled[0].payload).toEqual({paymentId: 10});
  });

  test('create payment action fail', async () => {
    axios.post.mockRejectedValue(new Error('This is error'));

    const thunk = createPayment();
    await thunk(dispatch, null, {client: axios, api: {}});
    const [pending, rejected] = dispatch.mock.calls;

    expect(pending[0].type).toBe(createPayment.pending().type);
    expect(rejected[0].type).toBe(createPayment.rejected().type);
    expect(rejected[0].payload).toBe('This is error');
  });

  test('loading payments async action', async () => {
    axios.get.mockReturnValue([{paymentId: 8}, {paymentId: 9}]);
    const api = {paymentByUserIdUrl: () => '/user/1/payments'};

    const thunk = loadPayments({id: 1});
    await thunk(dispatch, null, {client: axios, api});
    const [pending, fulfilled] = dispatch.mock.calls;
    const authHeader = axios.get.mock.calls[0][1].headers.Authorization;

    expect(authHeader).toBe('Bearer someusertoken');
    expect(pending[0].type).toBe(loadPayments.pending().type);
    expect(fulfilled[0].type).toBe(loadPayments.fulfilled().type);
    expect(fulfilled[0].payload).toEqual([{paymentId: 8}, {paymentId: 9}]);
  });

  test('loading payments action fail', async () => {
    axios.get.mockRejectedValue(new Error('This is error'));
    const api = {paymentByUserIdUrl: () => '/users/1/payments'};

    const thunk = loadPayments({id: 1});
    await thunk(dispatch, null, {client: axios, api});
    const [pending, rejected] = dispatch.mock.calls;

    expect(pending[0].type).toBe(loadPayments.pending().type);
    expect(rejected[0].type).toBe(loadPayments.rejected().type);
    expect(rejected[0].payload).toBe('This is error');
  });

  describe('load orders async action', () => {
    axios.get.mockReturnValue([{orderId: 1}, {orderId: 2}]);
    const payments = {
      payments: [
        {
          id: 1,
          ordersStatus: 'idle',
        },
        {
          id: 2,
          ordersStatus: 'loading',
        },
      ],
    };

    test('load orders async action subsequent request', async () => {
      axios.get.mockRejectedValue(new Error('This is error'));
      const api = {ordersByPaymentIdUrl: () => '/payments/1/orders'};
      const getState = jest.fn().mockReturnValue({payments});

      const thunk = loadOrders({id: 1});
      await thunk(dispatch, getState, {client: axios, api});
      const [pending, fulfilled] = dispatch.mock.calls;

      expect(pending[0].type).toBe(loadOrders.pending().type);
      expect(fulfilled[0].type).toBe(loadOrders.fulfilled().type);
      expect(fulfilled[0].payload).toEqual(undefined);
    });
  });

  afterAll(() => {
    localStorage.clear();
  });
});

describe('payment reducer', () => {
  test('reset payment status', () => {
    const currentState = {
      status: 'rejected',
      payments: [{id: 1}, {id: 2}],
      error: 'this is error',
      page: {number: 0, size: 20, totalElements: 100, totalPages: 5},
    };

    const actual = paymentReducer(currentState, resetPaymentStatus());
    const expected = {...currentState, status: 'idle', error: ''};

    expect(actual).toEqual(expected);
  });
});

describe('payment extra reducer', () => {
  const createState = (status, payments, error, page) => ({
    status,
    payments,
    error,
    page,
  });

  test('create payment pending', () => {
    const initialState = createState('rejected', [], 'this is error', {
      number: 0,
    });

    const actual = paymentReducer(initialState, createPayment.pending());
    const expected = {...initialState, status: 'loading', error: ''};

    expect(actual).toEqual(expected);
  });

  test('create payment rejected', () => {
    const initialState = createState('loading', [], '', {number: 0});
    const rejectedAction = {
      type: createPayment.rejected.type,
      payload: 'this is error',
    };

    const actual = paymentReducer(initialState, rejectedAction);
    const expected = {
      ...initialState,
      status: 'rejected',
      error: 'this is error',
    };

    expect(actual).toEqual(expected);
  });

  test('create payment fulfilled', () => {
    const initialState = createState('loading', [], '', {number: 0});

    const actual = paymentReducer(initialState, createPayment.fulfilled());
    const expected = {...initialState, status: 'created'};

    expect(actual).toEqual(expected);
  });

  test('load payments pending', () => {
    const initialState = createState('rejected', [], 'this is error', {
      number: 0,
    });

    const actual = paymentReducer(initialState, loadPayments.pending());
    const expected = {...initialState, status: 'loading', error: ''};

    expect(actual).toEqual(expected);
  });

  test('load payments rejected', () => {
    const initialState = createState('loading', [], '', {number: 0});
    const rejectedAction = {
      type: createPayment.rejected.type,
      payload: 'this is error',
    };

    const actual = paymentReducer(initialState, rejectedAction);
    const expected = {
      ...initialState,
      status: 'rejected',
      error: 'this is error',
    };

    expect(actual).toEqual(expected);
  });

  test('load payments fulfilled', () => {
    const initialState = createState('loading', [], '', {
      number: 0,
      size: 20,
      totalElements: 0,
      totalPages: 0,
    });
    const payload = {
      data: {
        _embedded: {
          paymentDtoes: [{id: 1}, {id: 2}],
        },
        page: {number: 2, size: 20, totalElements: 40, totalPages: 2},
      },
    };

    const actual = paymentReducer(
      initialState,
      loadPayments.fulfilled(payload)
    );
    const expected = createState('idle', [{id: 1}, {id: 2}], '', {
      number: 2,
      size: 20,
      totalElements: 40,
      totalPages: 2,
    });

    expect(actual).toEqual(expected);
  });

  describe('load orders actions', () => {
    const payments = [
      {
        id: 1,
        ordersStatus: 'loading',
        ordersError: '',
        currentRequestId: undefined,
        orders: [{id: 1}, {id: 2}],
        ordersPage: {number: 0, size: 20, totalElements: 40, totalPages: 2},
      },
      {
        id: 2,
        ordersStatus: 'loading',
        ordersError: '',
        currentRequestId: undefined,
      },
      {
        id: 3,
        ordersStatus: 'rejected',
        ordersError: 'this is error',
        currentRequestId: undefined,
      },
    ];

    const page = {number: 0, size: 20, totalElements: 0, totalPages: 0};

    test('load orders pending', () => {
      const initialState = createState('idle', payments, '', page);
      const expectedPayments = [...payments];
      expectedPayments[2] = {
        id: 3,
        ordersStatus: 'loading',
        ordersError: '',
        currentRequestId: 'asdfg',
      };

      const pendingAction = {
        type: loadOrders.pending.type,
        meta: {arg: {id: 3}, requestId: 'asdfg'},
      };

      const actual = paymentReducer(initialState, pendingAction);
      const expected = createState('idle', expectedPayments, '', page);

      expect(actual).toEqual(expected);
    });

    test('load orders pending when order status is loading', () => {
      const initialState = createState('idle', payments, '', page);
      const pendingAction = {
        type: loadOrders.pending.type,
        meta: {arg: {id: 2}},
      };

      const actual = paymentReducer(initialState, pendingAction);

      expect(actual).toEqual(initialState);
    });

    test('load orders fulfilled when orders empty', () => {
      const initialState = createState('idle', payments, '', page);
      const expectedPayments = [...payments];
      expectedPayments[1] = {
        id: 2,
        ordersStatus: 'idle',
        orders: [{id: 1}, {id: 2}],
        ordersError: '',
        currentRequestId: undefined,
        ordersPage: {number: 0, size: 20, totalElements: 40, totalPages: 2},
      };

      const fulfilledAction = {
        type: loadOrders.fulfilled.type,
        payload: {
          data: {
            _embedded: {
              userOrderDtoes: [{id: 1}, {id: 2}],
            },
            page: {number: 0, size: 20, totalElements: 40, totalPages: 2},
          },
        },
        meta: {arg: {id: 2}},
      };

      const actual = paymentReducer(initialState, fulfilledAction);
      const expected = createState('idle', expectedPayments, '', page);

      expect(actual).toEqual(expected);
    });

    test('load orders fulfilled', () => {
      const initialState = createState('idle', payments, '', page);
      const expectedPayments = [...payments];
      expectedPayments[0] = {
        id: 1,
        ordersStatus: 'idle',
        orders: [{id: 1}, {id: 2}, {id: 3}, {id: 4}],
        ordersError: '',
        currentRequestId: undefined,
        ordersPage: {number: 1, size: 20, totalElements: 40, totalPages: 2},
      };

      const fulfilledAction = {
        type: loadOrders.fulfilled.type,
        payload: {
          data: {
            _embedded: {
              userOrderDtoes: [{id: 3}, {id: 4}],
            },
            page: {number: 1, size: 20, totalElements: 40, totalPages: 2},
          },
        },
        meta: {arg: {id: 1}},
      };

      const actual = paymentReducer(initialState, fulfilledAction);
      const expected = createState('idle', expectedPayments, '', page);

      expect(actual).toEqual(expected);
    });

    test('load orders rejected', () => {
      const initialState = createState('idle', payments, '', page);
      const expectedPayments = [...payments];
      expectedPayments[1] = {
        id: 2,
        ordersStatus: 'rejected',
        ordersError: 'this is error',
        currentRequestId: undefined,
      };

      const rejectedAction = {
        type: loadOrders.rejected.type,
        payload: 'this is error',
        meta: {arg: {id: 2}},
      };

      const actual = paymentReducer(initialState, rejectedAction);
      const expected = createState('idle', expectedPayments, '', page);

      expect(actual).toEqual(expected);
    });
  });
});
