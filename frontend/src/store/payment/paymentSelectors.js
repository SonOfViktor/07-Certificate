export const selectPayments = state => state.payments;
export const selectPaymentsMeta = state => ({
  status: state.payments.status,
  error: state.payments.error,
});
