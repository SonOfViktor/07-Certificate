import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import CertificateTableRow from '../../../../components/admin/certificate/CertificateTableRow';
import {createCertificate} from '../../../helpers/certificateHelper';

describe('certificate table row', () => {
  const tbody = document.createElement('tbody');
  const certificate = createCertificate(
    2,
    'Certificate',
    'description',
    30,
    40,
    []
  );
  certificate.createDate = '01.01.2023';
  certificate.lastUpdateDate = '01.02.2023';

  test('certificate table row render', () => {
    render(<CertificateTableRow certificate={certificate} />, {
      container: document.body.appendChild(tbody),
    });

    expect(screen.getByText('Certificate')).toBeInTheDocument();
    expect(screen.getByText('40')).toBeInTheDocument();
    expect(screen.getByText('01.02.2023')).toBeInTheDocument();
  });

  test('click on open edit modal window button', () => {
    const handleModalOpenMock = jest.fn();

    render(
      <CertificateTableRow
        certificate={certificate}
        handleModalOpen={handleModalOpenMock}
      />, {
          container: document.body.appendChild(tbody),
        }
    );

    const openEditModalWindowButton = screen.getByTestId('ModeEditIcon');

    expect(handleModalOpenMock).not.toHaveBeenCalled();
    userEvent.click(openEditModalWindowButton);
    expect(handleModalOpenMock).toHaveBeenCalledWith(certificate);
  });

  test('click on open delete modal window button', () => {
    const handleModalOpenMock = jest.fn();

    render(
      <CertificateTableRow
        certificate={certificate}
        handleDeleteModalOpen={handleModalOpenMock}
      />, {
          container: document.body.appendChild(tbody),
        }
    );

    const openDeleteModalWindowButton = screen.getByTestId('DeleteIcon');

    expect(handleModalOpenMock).not.toHaveBeenCalled();
    userEvent.click(openDeleteModalWindowButton);
    expect(handleModalOpenMock).toHaveBeenCalledWith(certificate);
  });
});
