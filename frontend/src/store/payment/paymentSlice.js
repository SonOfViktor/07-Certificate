import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';

export const createPayment = createAsyncThunk(
  '@@payments/create-payment',
  async (data, {extra: {client, api}, rejectWithValue}) => {
    try {
      return await client.post(api.CREATE_PAYMENT, data, {
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('token'),
        },
      });
    } catch (e) {
      return rejectWithValue(e?.response?.data?.errorMessage || e.message);
    }
  }
);

export const loadPayments = createAsyncThunk(
  '@@payments/load-payments',
  async ({id, pageNumber = 1}, {extra: {client, api}, rejectWithValue}) => {
    try {
      return await client.get(api.paymentByUserIdUrl(id, pageNumber - 1), {
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('token'),
        },
      });
    } catch (e) {
      return rejectWithValue(e?.response?.data?.errorMessage || e.message);
    }
  }
);

export const loadOrders = createAsyncThunk(
  '@@payments/load-orders',
  async (
    {id, pageNumber = 0},
    {getState, requestId, extra: {client, api}, rejectWithValue}
  ) => {
    const payment = findPaymentById(getState().payments, id);
    try {
      if (
        payment.ordersStatus !== 'loading' ||
        requestId !== payment.currentRequestId
      ) {
        return;
      }

      return await client.get(api.ordersByPaymentIdUrl(id, pageNumber), {
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('token'),
        },
      });
    } catch (e) {
      return rejectWithValue(e?.response?.data?.errorMessage || e.message);
    }
  }
);

const initialState = {
  status: 'idle',
  payments: [],
  error: '',
  page: {number: 0, size: 20, totalElements: 0, totalPages: 0},
};

const paymentSlice = createSlice({
  name: '@@payments',
  initialState,
  reducers: {
    resetPaymentStatus: state => {
      state.status = 'idle';
      state.error = '';
    },
  },
  extraReducers: builder => {
    builder
      .addCase(createPayment.fulfilled, state => {
        state.status = 'created';
      })
      .addCase(loadPayments.fulfilled, (state, action) => {
        state.status = 'idle';
        state.payments = action.payload.data._embedded.paymentDtoes;
        state.page = action.payload.data.page;
      })
      .addCase(loadOrders.pending, (state, action) => {
        const payment = findPaymentById(state, action.meta.arg.id);
        if (payment.ordersStatus !== 'loading') {
          payment.ordersStatus = 'loading';
          payment.ordersError = '';
          payment.currentRequestId = action.meta.requestId;
        }
      })
      .addCase(loadOrders.fulfilled, (state, action) => {
        const payment = findPaymentById(state, action.meta.arg.id);
        payment.ordersStatus = 'idle';
        payment.ordersPage = action.payload.data.page;

        if (payment.orders) {
          payment.orders.push(...action.payload.data._embedded.userOrderDtoes);
        } else {
          payment.orders = action.payload.data._embedded.userOrderDtoes;
        }
      })
      .addCase(loadOrders.rejected, (state, action) => {
        const payment = findPaymentById(state, action.meta.arg.id);
        payment.ordersStatus = 'rejected';
        payment.ordersError = action.payload;
      })
      .addMatcher(
        action => action.type.match(/^@@payments\/.*payment.*\/pending$/),
        state => {
          state.status = 'loading';
          state.error = '';
        }
      )
      .addMatcher(
        action => action.type.match(/^@@payments\/.*payment.*\/rejected$/),
        (state, action) => {
          state.status = 'rejected';
          state.error = action.payload;
        }
      );
  },
});

export const {resetPaymentStatus} = paymentSlice.actions;
export const paymentReducer = paymentSlice.reducer;

const findPaymentById = (state, id) => {
  return state.payments.find(payment => payment.id === id);
};
