export const createOrder = ratio => ({
  certificateName: `Certificate${ratio}`,
  duration: ratio * 5,
  cost: ratio * 10,
});

export const createOrderList = orderAmount =>
  new Array(orderAmount).fill(null).map((_, index) => createOrder(index + 1));

export const createPayment = (orderAmount, ratio) => ({
  id: ratio,
  createdDate: `2023-01-${ratio.toString().padStart(2, '0')}T08:00:00`,
  orders: createOrderList(orderAmount),
  ordersPage: {number: 0},
  ordersStatus: 'idle',
});

export const createPaymentList = (paymentAmount, orderAmount) =>
  new Array(paymentAmount)
    .fill(null)
    .map((_, index) => createPayment(orderAmount, index + 1));
