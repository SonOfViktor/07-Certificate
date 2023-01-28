import {render, screen} from '@testing-library/react';
import UserOrder from '../../../components/user/UserOrder';
import {createPayment} from './testData';

describe('user order component', () => {
  const payment = createPayment(1, 18);

  test('user order render', () => {
    render(<UserOrder payment={payment} order={payment.orders[0]} />);

    expect(screen.getByText('Certificate1')).toBeInTheDocument();
    expect(screen.getByText(/23.01.2023/)).toBeInTheDocument();
    expect(screen.getByText('$10')).toBeInTheDocument();
    expect(screen.getByText('Expired')).toBeInTheDocument();
  });
});
