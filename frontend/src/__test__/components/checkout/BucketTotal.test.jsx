import {render, screen} from '@testing-library/react';
import BucketTotal from '../../../components/checkout/BucketTotal';

describe('bucket total component', () => {
  test('bucket total render', () => {
    render(<BucketTotal total="500" />);

    expect(screen.getByText('$500')).toBeInTheDocument();
  });
});
