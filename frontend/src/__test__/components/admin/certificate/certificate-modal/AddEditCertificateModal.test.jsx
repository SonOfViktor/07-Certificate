import {screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import {renderWithMui} from '../../../../helpers/renderWithMui';
import * as reduxHooks from 'react-redux';
import AddEditCertificateModal from '../../../../../components/admin/certificate-modal/AddEditCertificateModal';
import {createCertificate} from '../../../../../store/certificates/certificateSlice';

jest.mock('react-redux');

describe('coupon list component', () => {
  const categoryList = ['Auto', 'Tech', 'Sport'];
  const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');
  const mockedUseSelector = jest.spyOn(reduxHooks, 'useSelector');

  const dispatch = jest.fn();

  beforeEach(() => {
    mockedUseDispatch.mockReturnValue(dispatch);
    mockedUseSelector.mockReturnValue(categoryList);
  });

  test('add certificate modal window render', () => {
    renderWithMui(<AddEditCertificateModal open={true} />);

    expect(screen.getByText('Add New Certificate')).toBeInTheDocument();
    expect(screen.queryByText('Edit Certificate')).toBeNull();
  });

  test('edit certificate modal window render', () => {
    const certificate = createCertificate();
    renderWithMui(
      <AddEditCertificateModal open={true} certificate={certificate} />
    );

    expect(screen.getByText('Edit Certificate')).toBeInTheDocument();
    expect(screen.queryByText('Add New Certificate')).toBeNull();
  });

  test('modal window close button', () => {
    const handleClose = jest.fn();

    renderWithMui(
      <AddEditCertificateModal open={true} handleClose={handleClose} />
    );

    const closeButton = screen.getByTestId('CloseIcon');
    userEvent.click(closeButton);

    expect(handleClose).toHaveBeenCalled();
  });
});
