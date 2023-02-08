import mediaQuery from 'css-mediaquery';

export const createMatchMedia = width => {
  return query => ({
    matches: mediaQuery.match(query, {
      width,
    }),
    addListener: jest.fn(),
    removeListener: jest.fn(),
  });
};
