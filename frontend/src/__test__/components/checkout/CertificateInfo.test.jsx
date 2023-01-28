import {render, screen} from '@testing-library/react';
import CertificateInfo from '../../../components/favorite/CertificateInfo';

describe('certificate info component', () => {
  test('certificate info render', () => {
    render(<CertificateInfo name="Certificate" description="description" />);

    expect(screen.getByText(/certificate/i)).toBeInTheDocument();
    expect(screen.getByText(/description/)).toBeInTheDocument();
  });
});
