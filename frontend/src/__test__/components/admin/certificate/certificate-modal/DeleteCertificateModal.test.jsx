import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import * as reduxHooks from 'react-redux';
import * as certificateAction from '../../../../../store/certificates/certificateSlice';
import {createCertificate} from '../../../../helpers/certificateHelper';
import DeleteCertificateModal from '../../../../../components/admin/certificate-modal/DeleteCertificateModal';

jest.mock('react-redux');

describe('coupon list component', () => {
  const certificate = createCertificate(3, 'Certificate');

  const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');
  const handleClose = jest.fn();
  const dispatch = jest.fn();

  beforeEach(() => {
    mockedUseDispatch.mockReturnValue(dispatch);
  });

  test('delete certificate modal window render', () => {
    render(<DeleteCertificateModal open={true} certificate={certificate} />);

    expect(screen.getByText('Delete Confirmation')).toBeInTheDocument();
    expect(
      screen.getByText(/do you .* certificate certificate/i)
    ).toBeInTheDocument();
  });

  test('click on no button', () => {
    render(<DeleteCertificateModal open={true} handleClose={handleClose} />);

    const noButton = screen.getByText(/no/i);
    userEvent.click(noButton);

    expect(handleClose).toHaveBeenCalled();
    expect(dispatch).not.toHaveBeenCalled();
  });

  test('click on cancel button', () => {
    render(<DeleteCertificateModal open={true} handleClose={handleClose} />);

    const cancelButton = screen.getByTestId('CloseIcon');
    userEvent.click(cancelButton);

    expect(handleClose).toHaveBeenCalled();
    expect(dispatch).not.toHaveBeenCalled();
  });

  test('click on yes button', () => {
    const deleteCertificate = jest.spyOn(
      certificateAction,
      'deleteCertificate'
    );

    render(
      <DeleteCertificateModal
        open={true}
        handleClose={handleClose}
        certificate={certificate}
      />
    );

    const yesButton = screen.getByText('Yes');
    userEvent.click(yesButton);

    expect(dispatch).toHaveBeenCalled();
    expect(deleteCertificate).toHaveBeenCalledWith(3);
    expect(handleClose).toHaveBeenCalled();
  });
});
