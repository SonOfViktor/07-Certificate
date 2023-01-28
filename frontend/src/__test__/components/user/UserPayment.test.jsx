import {render, screen} from '@testing-library/react';
import * as reduxHooks from 'react-redux';
import {createPayment} from './testData';
import UserPayment from '../../../components/user/UserPayment';

jest.mock('react-redux');

describe('user payment', () => {
  const payment = createPayment(3, 2);

  const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');
  const dispatch = jest.fn();

  beforeEach(() => {
    mockedUseDispatch.mockReturnValue(dispatch);
  });

  test('user payment', () => {
    render(
      <UserPayment
        payment={payment}
        expanded="2"
        handleAccordionExpand={jest.fn()}
      />
    );

    expect(screen.getByText('Payment #2')).toBeInTheDocument();
    expect(screen.getByText(/02.01.2023/)).toBeInTheDocument();
    expect(screen.getAllByText(/certificate/i)).toHaveLength(3);
  });
});
