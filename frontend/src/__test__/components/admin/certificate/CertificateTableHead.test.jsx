import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import CertificateTableHead from '../../../../components/admin/certificate/CertificateTableHead';

describe('certificate table head', () => {
  const table = document.createElement('table');

  test('certificate table head render', () => {
    render(<CertificateTableHead handleModalOpen={jest.fn()} />, {
      container: document.body.appendChild(table),
    });

    expect(screen.getByText('Id')).toBeInTheDocument();
    expect(screen.getByText('Name')).toBeInTheDocument();
    expect(screen.getByText('Duration')).toBeInTheDocument();
    expect(screen.getByText('Price')).toBeInTheDocument();
    expect(screen.getByText('Create Date')).toBeInTheDocument();
    expect(screen.getByText('Last Update Date')).toBeInTheDocument();
  });

  test('click on open modal window button', () => {
    const handleModalOpen = jest.fn();

    render(<CertificateTableHead handleModalOpen={handleModalOpen} />, {
      container: document.body.appendChild(table),
    });

    const openModalWindowButton = screen.getByTestId('NoteAddIcon');

    expect(handleModalOpen).not.toHaveBeenCalled();
    userEvent.click(openModalWindowButton);
    expect(handleModalOpen).toHaveBeenCalled();
  });
});
