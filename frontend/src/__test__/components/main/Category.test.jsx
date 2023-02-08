import {screen, render} from '@testing-library/react';
import Category from '../../../components/main/Category';

describe('category component on main page', () => {
  test('render category', () => {
    const props = {
      onClick: jest.fn(),
      name: 'Food',
    };

    render(<Category {...props} />);

    const button = screen.getByRole('button');
    const image = screen.getByAltText(/food/i);
    const text = screen.getByText(/food/i);

    expect(button).toContainElement(image);
    expect(image).toHaveAttribute('src', '/api/v1/categories/Food/image');
    expect(text).toBeInTheDocument();
  });
});
