import {render, screen} from '@testing-library/react';
import Footer from '../../../components/footer/Footer';

describe('footer component', () => {
  test('footer render', () => {
    render(<Footer />);

    const links = screen.getAllByRole('link');

    expect(screen.getByText(/fairy company/i)).toBeInTheDocument();
    expect(links[0]).toHaveAttribute(
      'href',
      'https://linkedin.com/in/maksim-silevich'
    );
    expect(links[1]).toHaveAttribute(
      'href',
      'https://github.com/SonOfViktor/Module7_Frontend'
    );
  });
});
