import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import {useSelector, useDispatch} from 'react-redux';
import {useNavigate} from 'react-router-dom';
import Bucket from '../../../components/checkout/Bucket';
import * as bucketActions from '../../../store/bucket/bucketSlice';
import * as paymentActions from '../../../store/payment/paymentSlice';
import {createCertificateList} from '../../helpers/certificateHelper';

jest.mock('react-router-dom', () => ({
  useNavigate: jest.fn(),
}));

jest.mock('react-redux', () => ({
  useSelector: jest.fn(),
  useDispatch: jest.fn(),
}));

describe('bucket component', () => {
  const dispatch = jest.fn();
  const certificates = createCertificateList(2);
  certificates.forEach((cert, index) => (cert.amount = index + 1));

  beforeEach(() => {
    useDispatch.mockReturnValue(dispatch);
    useSelector
      .mockReturnValueOnce(certificates)
      .mockReturnValueOnce(500)
      .mockReturnValue({status: 'created', error: ''});
  });

  test('bucket render', () => {
    render(<Bucket />);

    expect(screen.getAllByText(/certificate/i).length).toBe(2);
    expect(screen.getByText(/purchase/i)).toBeInTheDocument();
  });

  test('bucket actions when payment created', () => {
    const resetBucketAction = jest.spyOn(bucketActions, 'resetBucket');
    const resetPaymentStatusAction = jest.spyOn(
      paymentActions,
      'resetPaymentStatus'
    );

    render(<Bucket />);

    expect(dispatch).toHaveBeenCalledTimes(2);
    expect(resetBucketAction).toHaveBeenCalled();
    expect(resetPaymentStatusAction).toHaveBeenCalled();
  });

  test('click on back button', () => {
    const navigate = jest.fn();
    useNavigate.mockReturnValue(navigate);

    render(<Bucket />);

    const backButton = screen.getByText('Back');

    expect(navigate).not.toHaveBeenCalled();
    userEvent.click(backButton);
    expect(navigate).toHaveBeenCalledWith(-1);
  });

  test('click on buy button', () => {
    const createPaymentAction = jest.spyOn(paymentActions, 'createPayment');

    render(<Bucket />);

    const buyButton = screen.getByText('Buy');

    expect(createPaymentAction).not.toHaveBeenCalled();
    userEvent.click(buyButton);
    expect(createPaymentAction).toHaveBeenCalledWith([1, 2, 2]);
  });

  test('certificates list is empty', () => {
    useSelector.mockReset();
    useSelector
      .mockReturnValueOnce([])
      .mockReturnValueOnce(500)
      .mockReturnValue({status: 'idle', error: ''});

    render(<Bucket />);

    const buyButton = screen.getByText('Buy');

    expect(buyButton).toBeDisabled();
  });
});
