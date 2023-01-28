import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import * as reduxHooks from 'react-redux';
import BucketItem from '../../../components/checkout/BucketItem';
import * as bucketActions from '../../../store/bucket/bucketSlice';
import {createCertificate} from '../../helpers/certificateHelper';

jest.mock('react-redux');
jest.mock('react-router-dom');

describe('coupon list component', () => {
  const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');
  const dispatch = jest.fn();
  const certificate = createCertificate(
    1,
    'Certificate1',
    'description',
    0,
    50
  );
  certificate.amount = 1;

  beforeEach(() => {
    mockedUseDispatch.mockReturnValue(dispatch);
  });

  test('bucket item render', () => {
    render(<BucketItem certificate={certificate} />);

    expect(screen.getByAltText('Certificate1')).toHaveAttribute(
      'src',
      '/api/v1/certificates/1/image'
    );
    expect(screen.getByText('Certificate1')).toBeInTheDocument();
    expect(screen.getByText('1')).toBeInTheDocument();
    expect(screen.getByText('$50')).toBeInTheDocument();
    expect(screen.getByTestId('RemoveIcon')).toBeInTheDocument();
    expect(screen.getByTestId('AddIcon')).toBeInTheDocument();
  });

  test('click on remove icon', () => {
    const deleteActionMock = jest.spyOn(
      bucketActions,
      'deleteBucketCertificate'
    );

    render(<BucketItem certificate={certificate} />);

    const removeIcon = screen.getByTestId('RemoveIcon');

    expect(dispatch).not.toHaveBeenCalled();
    userEvent.click(removeIcon);
    expect(dispatch).toHaveBeenCalled();
    expect(deleteActionMock).toHaveBeenCalledWith(1);
  });

  test('click on add icon', () => {
    const addActionMock = jest.spyOn(bucketActions, 'addBucketCertificate');

    render(<BucketItem certificate={certificate} />);

    const addIcon = screen.getByTestId('AddIcon');

    expect(dispatch).not.toHaveBeenCalled();
    userEvent.click(addIcon);
    expect(dispatch).toHaveBeenCalled();
    expect(addActionMock).toHaveBeenCalledWith(certificate);
  });
});
