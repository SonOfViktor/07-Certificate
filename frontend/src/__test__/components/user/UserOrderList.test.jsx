import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import * as reduxHooks from 'react-redux';
import * as paymentActions from '../../../store/payment/paymentSlice';
import UserOrderList from '../../../components/user/UserOrderList';
import {createPayment} from './testData';

jest.mock('react-redux');

describe('user order list', () => {
  const payment = createPayment(3, 2);

  const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');
  const dispatch = jest.fn();

  beforeEach(() => {
    mockedUseDispatch.mockReturnValue(dispatch);
  });

  test('user order list render', () => {
    payment.ordersPage.totalPages = 1;

    render(<UserOrderList payment={payment} />);

    expect(screen.getAllByText(/certificate/i)).toHaveLength(3);
    expect(screen.queryByRole('button', {name: 'More'})).toBeNull();
  });

  test('show more orders', () => {
    payment.ordersPage.totalPages = 3;
    const loadOrders = jest.spyOn(paymentActions, 'loadOrders');

    render(<UserOrderList payment={payment} />);

    const moreButton = screen.getByRole('button', {name: 'More'});
    expect(dispatch).not.toHaveBeenCalled();

    userEvent.click(moreButton);
    expect(dispatch).toHaveBeenCalled();
    expect(loadOrders).toHaveBeenCalledWith({id: 2, pageNumber: 1});
  });
});
