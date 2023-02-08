import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import * as reduxHooks from 'react-redux';
import * as routerHooks from 'react-router-dom';
import * as paymentActions from '../../../store/payment/paymentSlice';
import {createPaymentList} from './testData';
import UserPaymentList from '../../../components/user/UserPaymentList';

jest.mock('react-redux');
jest.mock('react-router-dom');

describe('user payment list', () => {
  const user = {id: 1};
  const payments = createPaymentList(3, 3);

  const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');
  const mockedUseSelector = jest.spyOn(reduxHooks, 'useSelector');
  const mockedUseSearchParams = jest.spyOn(routerHooks, 'useSearchParams');
  const dispatch = jest.fn();
  const get = jest.fn();
  const setSearchParams = jest.fn();

  beforeEach(() => {
    mockedUseDispatch.mockReturnValue(dispatch);
    mockedUseSelector.mockReturnValue({
      status: 'idle',
      payments,
      error: '',
      page: {totalPages: 5},
    });
    mockedUseSearchParams.mockReturnValue([{get}, setSearchParams]);
  });

  test('user payment list render', () => {
    const loadPayments = jest.spyOn(paymentActions, 'loadPayments');

    render(<UserPaymentList user={user} />);

    expect(screen.getAllByText(/payment #/i)).toHaveLength(3);
    expect(screen.getAllByText(/certificate/i)).toHaveLength(9);
    expect(get).toHaveBeenCalledWith('page');
    expect(loadPayments).toHaveBeenCalledWith({id: 1, pageNumber: 1});
  });

  test('open payment first time', () => {
    payments[0].orders = null;
    const loadOrders = jest.spyOn(paymentActions, 'loadOrders');

    render(<UserPaymentList user={user} />);

    const expendButtons = screen.getAllByTestId('ExpandMoreIcon');

    expect(dispatch).not.toHaveBeenCalledTimes(2);
    userEvent.click(expendButtons[0]);

    expect(dispatch).toHaveBeenCalledTimes(2);
    expect(loadOrders).toHaveBeenCalledWith({id: 1});
  });

  test('open payment with loaded orders', () => {
    const loadOrders = jest.spyOn(paymentActions, 'loadOrders');

    render(<UserPaymentList user={user} />);

    const expendButtons = screen.getAllByTestId('ExpandMoreIcon');

    userEvent.click(expendButtons[1]);
    expect(loadOrders).not.toHaveBeenCalled();
  });

  test('click page button', () => {
    get.mockReturnValue(3);

    render(<UserPaymentList user={user} />);

    expect(screen.getByRole('button', {name: 'page 3'})).toBeInTheDocument();

    userEvent.click(screen.getByRole('button', {name: 'Go to page 1'}));
    userEvent.click(screen.getByTestId('NavigateNextIcon'));
    userEvent.click(screen.getByTestId('NavigateBeforeIcon'));

    expect(setSearchParams).toHaveBeenCalledTimes(3);
  });
});
